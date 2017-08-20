/*
 * Copyright © 2017 Xiamen University.All Rights Reserved.
 */
package cn.edu.xmu.bip.service.impl;

import cn.com.lx1992.lib.client.constant.CommonConstant;
import cn.com.lx1992.lib.client.util.JsonUtil;
import cn.com.lx1992.lib.client.util.PreferencesUtil;
import cn.edu.xmu.bip.constant.PreferenceKeyConstant;
import cn.edu.xmu.bip.constant.ResourceConstant;
import cn.edu.xmu.bip.dao.IMessageDAO;
import cn.edu.xmu.bip.dao.factory.DAOFactory;
import cn.edu.xmu.bip.domain.MessageDO;
import cn.edu.xmu.bip.exception.ClientException;
import cn.edu.xmu.bip.exception.ClientExceptionEnum;
import cn.edu.xmu.bip.message.AckMessage;
import cn.edu.xmu.bip.message.BaseReceiveMessage;
import cn.edu.xmu.bip.message.FingerprintEnrollFailureMessage;
import cn.edu.xmu.bip.message.FingerprintEnrollMessage;
import cn.edu.xmu.bip.message.FingerprintEnrollReplyMessage;
import cn.edu.xmu.bip.message.FingerprintEnrollSuccessMessage;
import cn.edu.xmu.bip.message.FingerprintIdentifyFailureMessage;
import cn.edu.xmu.bip.message.FingerprintIdentifyMessage;
import cn.edu.xmu.bip.message.FingerprintIdentifyReplyMessage;
import cn.edu.xmu.bip.message.FingerprintIdentifySuccessMessage;
import cn.edu.xmu.bip.message.GeneralBusinessFailureMessage;
import cn.edu.xmu.bip.message.GeneralBusinessMessage;
import cn.edu.xmu.bip.message.GeneralBusinessSuccessMessage;
import cn.edu.xmu.bip.message.ServiceCancelMessage;
import cn.edu.xmu.bip.message.ServicePauseMessage;
import cn.edu.xmu.bip.message.ServiceResumeMessage;
import cn.edu.xmu.bip.message.UpdateCompanyInfoMessage;
import cn.edu.xmu.bip.message.UpdateCounterInfoMessage;
import cn.edu.xmu.bip.message.UpdateUserInfoMessage;
import cn.edu.xmu.bip.meta.MessageTypeEnum;
import cn.edu.xmu.bip.meta.ResourceTypeEnum;
import cn.edu.xmu.bip.service.IFingerprintService;
import cn.edu.xmu.bip.service.IMessageService;
import cn.edu.xmu.bip.service.IResourceService;
import cn.edu.xmu.bip.service.IWSService;
import cn.edu.xmu.bip.service.factory.ServiceFactory;
import cn.edu.xmu.bip.ui.main.model.BannerModel;
import cn.edu.xmu.bip.ui.main.model.CounterModel;
import cn.edu.xmu.bip.ui.main.model.MessageModel;
import cn.edu.xmu.bip.ui.main.model.PaneVisibleModel;
import cn.edu.xmu.bip.ui.main.model.UserModel;
import cn.edu.xmu.bip.util.VoiceUtil;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MessageServiceImpl implements IMessageService {
    //服务状态：忙碌/空闲/停止
    private static final String SERVICE_STATE_BUSY = "busy";
    private static final String SERVICE_STATE_IDLE = "idle";
    private static final String SERVICE_STATE_STOP = "stop";
    //服务类型：一般业务/指纹登记/指纹辨识
    private static final String SERVICE_TYPE_GENERAL_BUSINESS = "general-business";
    private static final String SERVICE_TYPE_FINGERPRINT_ENROLL = "fingerprint-enroll";
    private static final String SERVICE_TYPE_FINGERPRINT_IDENTIFY = "fingerprint-identify";
    //指纹登记/辨识回复状态：错误/超时/(模板)提取
    private static final String FINGERPRINT_STATUS_ERROR = "error";
    private static final String FINGERPRINT_STATUS_TIMEOUT = "timeout";
    private static final String FINGERPRINT_STATUS_EXTRACT = "extract";
    //超时：返回主屏超时/仅展示用户超时
    private static final int RETURN_HOME_TIMEOUT = 10;
    private static final int USER_SHOW_ONLY_TIMEOUT = 5;

    private final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    //当前消息
    private BaseReceiveMessage current;
    //线程池&延迟任务
    private ScheduledFuture<?> future;
    private ScheduledExecutorService executor;

    @Inject
    private ServiceFactory serviceFactory;
    @Inject
    private DAOFactory daoFactory;

    @Inject
    private UserModel userModel;
    @Inject
    private BannerModel bannerModel;
    @Inject
    private CounterModel counterModel;
    @Inject
    private MessageModel messageModel;
    @Inject
    private PaneVisibleModel paneVisibleModel;

    @PostConstruct
    private void initialize() {
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    @PreDestroy
    private void destroy() throws InterruptedException {
        //清除上次服务状态和服务类型，确保下次可以立即开始服务
        PreferencesUtil.put(PreferenceKeyConstant.SERVICE_STATE, SERVICE_STATE_IDLE);
        PreferencesUtil.remove(PreferenceKeyConstant.SERVICE_TYPE);

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);
    }

    @Override
    public void receive(String body) {
        //在单线程池中排队运行，保证按序处理，且不占用WebSocket线程
        CompletableFuture
                .supplyAsync(() -> process(body), executor)
                .thenAccept(FutureTask::run);
    }

    //消息一次处理方法
    //-----------------------------------------------------------------------

    /**
     * 消息一次处理
     *
     * @param body 消息体
     * @return 实现二次处理的Task
     */
    private Task<Void> process(String body) {
        //解析消息
        BaseReceiveMessage message = JsonUtil.toObject(body, BaseReceiveMessage.class);
        MessageTypeEnum type = MessageTypeEnum.getByType(message.getType());

        //发送ACK，检查幂等，保存日志
        replyAck(message.getUid());
        checkIdempotent(message.getUid());
        createLog(message.getUid(), message.getType(), body);

        //具体处理
        return process(type, body);
    }

    /**
     * ACK收到的消息
     *
     * @param uid 消息UID
     */
    private void replyAck(Long uid) {
        AckMessage message = new AckMessage();
        message.setUid(uid);

        IWSService wsService = (IWSService) serviceFactory.getInstance(ServiceFactory.WS);
        wsService.sendMessage(JsonUtil.toJson(message));
    }

    /**
     * 检查消息幂等
     *
     * @param uid UID
     */
    private void checkIdempotent(Long uid) {
        IMessageDAO messageDAO = (IMessageDAO) daoFactory.getInstance(DAOFactory.MESSAGE);
        MessageDO domain = messageDAO.selectOne(uid);
        if (domain != null) {
            logger.error("message {} break the idempotent principle");
            throw new ClientException(ClientExceptionEnum.MESSAGE_IDEMPOTENT_BROKEN);
        }
    }

    /**
     * 保存消息日志
     *
     * @param uid  UID
     * @param type 类型
     * @param body 消息体
     */
    private void createLog(Long uid, String type, String body) {
        IMessageDAO messageDAO = (IMessageDAO) daoFactory.getInstance(DAOFactory.MESSAGE);
        messageDAO.insert(uid, type, body);
    }


    //消息处理逻辑
    //-----------------------------------------------------------------------

    /**
     * 根据首次解析出的消息类型
     * 二次解析消息进行具体处理
     *
     * @param type 消息类型
     * @param body 消息体
     * @return 实现具体处理过程的Task
     */
    private Task<Void> process(MessageTypeEnum type, String body) {
        //二次解析出的消息保存到类变量中，处理过程中需要被共享
        switch (type) {
            case SERVICE_CANCEL:
                current = JsonUtil.toObject(body, ServiceCancelMessage.class);
                prepareResource(current.getResources());
                return processServiceCancel();
            case SERVICE_PAUSE:
                current = JsonUtil.toObject(body, ServicePauseMessage.class);
                prepareResource(current.getResources());
                return processServicePause();
            case SERVICE_RESUME:
                current = JsonUtil.toObject(body, ServiceResumeMessage.class);
                prepareResource(current.getResources());
                return processServiceResume();
            case GENERAL_BUSINESS:
                current = JsonUtil.toObject(body, GeneralBusinessMessage.class);
                prepareResource(current.getResources());
                return processGeneralBusiness();
            case GENERAL_BUSINESS_SUCCESS:
                current = JsonUtil.toObject(body, GeneralBusinessSuccessMessage.class);
                prepareResource(current.getResources());
                return processGeneralBusinessSuccess();
            case GENERAL_BUSINESS_FAILURE:
                current = JsonUtil.toObject(body, GeneralBusinessFailureMessage.class);
                prepareResource(current.getResources());
                return processGeneralBusinessFailure();
            case FINGERPRINT_ENROLL:
                current = JsonUtil.toObject(body, FingerprintEnrollMessage.class);
                prepareResource(current.getResources());
                return processFingerprintEnroll();
            case FINGERPRINT_ENROLL_SUCCESS:
                current = JsonUtil.toObject(body, FingerprintEnrollSuccessMessage.class);
                prepareResource(current.getResources());
                return processFingerprintEnrollSuccess();
            case FINGERPRINT_ENROLL_FAILURE:
                current = JsonUtil.toObject(body, FingerprintEnrollFailureMessage.class);
                prepareResource(current.getResources());
                return processFingerprintEnrollFailure();
            case FINGERPRINT_IDENTIFY:
                current = JsonUtil.toObject(body, FingerprintIdentifyMessage.class);
                prepareResource(current.getResources());
                return processFingerprintIdentify();
            case FINGERPRINT_IDENTIFY_SUCCESS:
                current = JsonUtil.toObject(body, FingerprintIdentifySuccessMessage.class);
                prepareResource(current.getResources());
                return processFingerprintIdentifySuccess();
            case FINGERPRINT_IDENTIFY_FAILURE:
                current = JsonUtil.toObject(body, FingerprintIdentifyFailureMessage.class);
                prepareResource(current.getResources());
                return processFingerprintIdentifyFailure();
            case UPDATE_COMPANY_INFO:
                current = JsonUtil.toObject(body, UpdateCompanyInfoMessage.class);
                prepareResource(current.getResources());
                return processUpdateCompanyInfo();
            case UPDATE_COUNTER_INFO:
                current = JsonUtil.toObject(body, UpdateCounterInfoMessage.class);
                prepareResource(current.getResources());
                return processUpdateCounterInfo();
            case UPDATE_USER_INFO:
                current = JsonUtil.toObject(body, UpdateUserInfoMessage.class);
                prepareResource(current.getResources());
                return processUpdateUserInfo();
            default:
                logger.error("unknown message type {}", type);
                throw new ClientException(ClientExceptionEnum.MESSAGE_UNKNOWN_TYPE);
        }
    }

    /**
     * 准备消息资源
     * 将远程URL转换成本地路径
     *
     * @param resources 资源
     */
    private void prepareResource(List<BaseReceiveMessage.Resource> resources) {
        if (!CollectionUtils.isEmpty(resources)) {
            resources.forEach(resource -> {
                IResourceService resourceService =
                        (IResourceService) serviceFactory.getInstance(ServiceFactory.RESOURCE);
                //ImageView控件限制，图片路径需要转换成URI格式；声音直接支持路径
                String image = resourceService.get(ResourceTypeEnum.IMAGE, resource.getImage(), true);
                List<String> voices = resourceService.get(ResourceTypeEnum.VOICE, resource.getVoices(), false).stream()
                        .filter(voice -> !StringUtils.isEmpty(voice))
                        .collect(Collectors.toList());
                resource.setImage(image);
                resource.setVoices(voices);
            });
        }
    }

    //消息具体(分类型)处理逻辑
    //-----------------------------------------------------------------------

    /**
     * 处理“取消服务”消息
     * <p>
     * 1.前置条件 状态=忙绿
     * 2.后置条件 状态=空闲
     * 3.取消指纹SDK操作(如果有需要)
     * 4.播放声音和更新UI
     */
    private Task<Void> processServiceCancel() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                checkPreCondition(SERVICE_STATE_BUSY, (String) null);
                //TODO
                cancelOngoingService();
                updatePostCondition(SERVICE_STATE_IDLE, null);
                return null;
            }

            @Override
            protected void succeeded() {
                BaseReceiveMessage.Resource resource = current.getResources().get(ResourceConstant.INDEX_DEFAULT);
                VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                //立即返回主屏幕，等同于立即调用returnHome()
                clearUserPaneAndHide();
                clearMessagePaneAndHide();
                logger.info("process {} message {}", current.getType(), current.getUid());
            }

            @Override
            protected void failed() {
                Throwable throwable = getException();
                logger.error("process {} message {} failed", current.getType(), current.getUid(), throwable);
            }
        };
    }

    /**
     * 处理“暂停服务”消息
     * <p>
     * 1.前置条件 状态=空闲
     * 2.后置条件 状态=停止
     * 3.播放声音和更新UI
     */
    private Task<Void> processServicePause() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                checkPreCondition(SERVICE_STATE_IDLE, (String) null);
                updatePostCondition(SERVICE_STATE_STOP, null);
                return null;
            }

            @Override
            protected void succeeded() {
                BaseReceiveMessage.Resource resource = current.getResources().get(ResourceConstant.INDEX_DEFAULT);
                VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                updateCounterPaneOrShow(null, null, true, true);
                logger.info("process {} message {}", current.getType(), current.getUid());
            }

            @Override
            protected void failed() {
                Throwable throwable = getException();
                logger.error("process {} message {} failed", current.getType(), current.getUid(), throwable);
            }
        };
    }

    /**
     * 处理“恢复服务”消息
     * <p>
     * 1.前置条件 状态=停止
     * 2.后置条件 状态=空闲
     * 3.播放声音和更新UI
     */
    private Task<Void> processServiceResume() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                checkPreCondition(SERVICE_STATE_STOP, (String) null);
                updatePostCondition(SERVICE_STATE_IDLE, null);
                return null;
            }

            @Override
            protected void succeeded() {
                BaseReceiveMessage.Resource resource = current.getResources().get(ResourceConstant.INDEX_DEFAULT);
                VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                updateCounterPaneOrShow(null, null, false, true);
                logger.info("process {} message {}", current.getType(), current.getUid());
            }

            @Override
            protected void failed() {
                Throwable throwable = getException();
                logger.error("process {} message {} failed", current.getType(), current.getUid(), throwable);
            }
        };
    }

    /**
     * 处理“一般业务”消息
     * <p>
     * 1.前置条件 状态=空闲
     * 2.后置条件 状态=忙碌/类型=一般业务
     * 3.播放声音和更新UI
     * 4.计划timeout秒超时
     */
    private Task<Void> processGeneralBusiness() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                checkPreCondition(SERVICE_STATE_IDLE, (String) null);
                updatePostCondition(SERVICE_STATE_BUSY, SERVICE_TYPE_GENERAL_BUSINESS);
                scheduleTask(() -> timeoutOngoingService(), ((GeneralBusinessMessage) current).getTimeout());
                return null;
            }

            @Override
            protected void succeeded() {
                BaseReceiveMessage.Resource resource = current.getResources().get(ResourceConstant.INDEX_GB_WAIT);
                VoiceUtil.add(resource.getVoices(), VoiceUtil.LOW_PRIORITY);
                updateMessagePaneAndShow(resource.getImage(),
                        ((GeneralBusinessMessage) current).getExtras().get(ResourceConstant.INDEX_GB_WAIT));
                logger.info("process {} message {}", current.getType(), current.getUid());
            }

            @Override
            protected void failed() {
                Throwable throwable = getException();
                logger.error("process {} message {} failed", current.getType(), current.getUid(), throwable);
            }
        };
    }

    /**
     * 处理“一般业务受理成功”消息
     * <p>
     * 1.前置条件 状态=忙碌/类型=一般业务
     * 2.后置条件 状态=空闲
     * 3.播放声音和更新UI
     * 4.计划10秒返回主屏
     */
    private Task<Void> processGeneralBusinessSuccess() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                checkPreCondition(SERVICE_STATE_BUSY, SERVICE_TYPE_GENERAL_BUSINESS);
                updatePostCondition(SERVICE_STATE_IDLE, null);
                scheduleTask(returnHome(), RETURN_HOME_TIMEOUT);
                return null;
            }

            @Override
            protected void succeeded() {
                BaseReceiveMessage.Resource resource = current.getResources().get(ResourceConstant.INDEX_DEFAULT);
                VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                updateMessagePaneAndShow(resource.getImage(), ((GeneralBusinessSuccessMessage) current).getExtra());
                logger.info("process {} message {}", current.getType(), current.getUid());
            }

            @Override
            protected void failed() {
                Throwable throwable = getException();
                logger.error("process {} message {} failed", current.getType(), current.getUid(), throwable);
            }
        };
    }

    /**
     * 处理“一般业务受理失败”消息
     * <p>
     * 1.前置条件 状态=忙碌/类型=一般业务
     * 2.后置条件 状态=空闲
     * 3.播放声音和更新UI
     * 4.计划10秒返回主屏
     */
    private Task<Void> processGeneralBusinessFailure() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                checkPreCondition(SERVICE_STATE_BUSY, SERVICE_TYPE_GENERAL_BUSINESS);
                updatePostCondition(SERVICE_STATE_IDLE, null);
                scheduleTask(returnHome(), RETURN_HOME_TIMEOUT);
                return null;
            }

            @Override
            protected void succeeded() {
                BaseReceiveMessage.Resource resource = current.getResources().get(ResourceConstant.INDEX_DEFAULT);
                VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                updateMessagePaneAndShow(resource.getImage(), ((GeneralBusinessFailureMessage) current).getExtra());
                logger.info("process {} message {}", current.getType(), current.getUid());
            }

            @Override
            protected void failed() {
                Throwable throwable = getException();
                logger.error("process {} message {} failed", current.getType(), current.getUid(), throwable);
            }
        };
    }

    /**
     * 处理“指纹登记”消息
     * <p>
     * 1.前置条件 状态=空闲
     * 2.后置条件 状态=忙碌/类型=指纹登记
     * 3.播放声音和更新UI
     * 4.计划timeout秒超时
     * 5.指纹仪SDK进入登记模式
     */
    private Task<Void> processFingerprintEnroll() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                checkPreCondition(SERVICE_STATE_IDLE, (String) null);
                updatePostCondition(SERVICE_STATE_BUSY, SERVICE_TYPE_FINGERPRINT_ENROLL);
                scheduleTask(() -> timeoutOngoingService(), ((FingerprintEnrollMessage) current).getTimeout());

                IFingerprintService fingerprintService =
                        (IFingerprintService) serviceFactory.getInstance(ServiceFactory.FINGERPRINT);
                fingerprintService.enroll(((FingerprintEnrollMessage) current).getTimes());
                return null;
            }

            @Override
            protected void succeeded() {
                BaseReceiveMessage.Resource resource =
                        current.getResources().get(ResourceConstant.INDEX_FE_1ST);
                updateMessagePaneAndShow(resource.getImage(),
                        ((FingerprintEnrollMessage) current).getExtras().get(ResourceConstant.INDEX_FE_1ST));
                VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                logger.info("process {} message {}", current.getType(), current.getUid());
            }

            @Override
            protected void failed() {
                Throwable throwable = getException();
                logger.error("process {} message {} failed", current.getType(), current.getUid(), throwable);
            }
        };
    }

    /**
     * 处理“指纹登记成功”消息
     * <p>
     * 1.前置条件 状态=忙碌/类型=指纹登记
     * 2.后置条件 状态=空闲
     * 3.播放声音和更新UI
     * 4.计划10秒返回主屏
     */
    private Task<Void> processFingerprintEnrollSuccess() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                checkPreCondition(SERVICE_STATE_BUSY, SERVICE_TYPE_FINGERPRINT_ENROLL);
                updatePostCondition(SERVICE_STATE_IDLE, null);
                scheduleTask(returnHome(), RETURN_HOME_TIMEOUT);
                return null;
            }

            @Override
            protected void succeeded() {
                BaseReceiveMessage.Resource resource = current.getResources().get(ResourceConstant.INDEX_DEFAULT);
                VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                updateMessagePaneAndShow(resource.getImage(), ((FingerprintEnrollSuccessMessage) current).getExtra());
                logger.info("process {} message {}", current.getType(), current.getUid());
            }

            @Override
            protected void failed() {
                Throwable throwable = getException();
                logger.error("process {} message {} failed", current.getType(), current.getUid(), throwable);
            }
        };
    }

    /**
     * 处理“指纹登记失败”消息
     * <p>
     * 1.前置条件 状态=忙碌/类型=指纹登记
     * 2.后置条件 状态=空闲
     * 3.播放声音和更新UI
     * 4.计划10秒返回主屏
     */
    private Task<Void> processFingerprintEnrollFailure() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                checkPreCondition(SERVICE_STATE_BUSY, SERVICE_TYPE_FINGERPRINT_ENROLL);
                updatePostCondition(SERVICE_STATE_IDLE, null);
                scheduleTask(returnHome(), RETURN_HOME_TIMEOUT);
                return null;
            }

            @Override
            protected void succeeded() {
                BaseReceiveMessage.Resource resource = current.getResources().get(ResourceConstant.INDEX_DEFAULT);
                VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                updateMessagePaneAndShow(resource.getImage(), ((FingerprintEnrollFailureMessage) current).getExtra());
                logger.info("process {} message {}", current.getType(), current.getUid());
            }

            @Override
            protected void failed() {
                Throwable throwable = getException();
                logger.error("process {} message {} failed", current.getType(), current.getUid(), throwable);
            }
        };
    }

    /**
     * 处理“指纹辨识”消息
     * <p>
     * 1.前置条件 状态=空闲
     * 2.后置条件 状态=忙碌/类型=指纹辨识
     * 3.播放声音和更新UI
     * 4.计划timeout秒超时
     * 5.指纹仪SDK进入辨识模式
     */
    private Task<Void> processFingerprintIdentify() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                checkPreCondition(SERVICE_STATE_IDLE, (String) null);
                updatePostCondition(SERVICE_STATE_BUSY, SERVICE_TYPE_FINGERPRINT_IDENTIFY);
                scheduleTask(() -> timeoutOngoingService(), ((FingerprintIdentifyMessage) current).getTimeout());

                IFingerprintService fingerprintService =
                        (IFingerprintService) serviceFactory.getInstance(ServiceFactory.FINGERPRINT);
                fingerprintService.identify();
                return null;
            }

            @Override
            protected void succeeded() {
                BaseReceiveMessage.Resource resource = current.getResources().get(ResourceConstant.INDEX_FI);
                VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                updateMessagePaneAndShow(resource.getImage(),
                        ((FingerprintIdentifyMessage) current).getExtras().get(ResourceConstant.INDEX_FI));
                logger.info("process {} message {}", current.getType(), current.getUid());
            }

            @Override
            protected void failed() {
                Throwable throwable = getException();
                logger.error("process {} message {} failed", current.getType(), current.getUid(), throwable);
            }
        };
    }

    /**
     * 处理“指纹辨识成功”消息
     * <p>
     * 1.前置条件 状态=忙碌/类型=指纹辨识
     * 2.后置条件 状态=空闲
     * 3.播放声音和更新UI
     * 4.计划10秒回主屏
     */
    private Task<Void> processFingerprintIdentifySuccess() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                checkPreCondition(SERVICE_STATE_BUSY, SERVICE_TYPE_FINGERPRINT_IDENTIFY);
                scheduleTask(returnHome(), RETURN_HOME_TIMEOUT);
                updatePostCondition(SERVICE_STATE_IDLE, null);
                return null;
            }

            @Override
            protected void succeeded() {
                BaseReceiveMessage.Resource resource = current.getResources().get(ResourceConstant.INDEX_DEFAULT);
                VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                updateMessagePaneAndShow(resource.getImage(), ((FingerprintIdentifySuccessMessage) current).getExtra());
                logger.info("process {} message {}", current.getType(), current.getUid());
            }

            @Override
            protected void failed() {
                Throwable throwable = getException();
                logger.error("process {} message {} failed", current.getType(), current.getUid(), throwable);
            }
        };
    }

    /**
     * 处理“指纹辨识失败”消息
     * <p>
     * 1.前置条件 状态=忙碌/类型=指纹辨识
     * 2.后置条件 状态=空闲
     * 3.播放声音和更新UI
     * 4.计划10秒回主屏
     */
    private Task<Void> processFingerprintIdentifyFailure() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                checkPreCondition(SERVICE_STATE_BUSY, SERVICE_TYPE_FINGERPRINT_IDENTIFY);
                scheduleTask(returnHome(), RETURN_HOME_TIMEOUT);
                updatePostCondition(SERVICE_STATE_IDLE, null);
                return null;
            }

            @Override
            protected void succeeded() {
                BaseReceiveMessage.Resource resource = current.getResources().get(ResourceConstant.INDEX_DEFAULT);
                VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                updateMessagePaneAndShow(resource.getImage(), ((FingerprintIdentifyFailureMessage) current).getExtra());
                logger.info("process {} message {}", current.getType(), current.getUid());
            }

            @Override
            protected void failed() {
                Throwable throwable = getException();
                logger.error("process {} message {} failed", current.getType(), current.getUid(), throwable);
            }
        };
    }

    /**
     * 处理“更新公司信息”消息
     * <p>
     * 1.更新ViewModel
     */
    private Task<Void> processUpdateCompanyInfo() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                return null;
            }

            @Override
            protected void succeeded() {
                BaseReceiveMessage.Resource resource = current.getResources().get(ResourceConstant.INDEX_DEFAULT);
                updateCompanyPane(resource.getImage(), ((UpdateCompanyInfoMessage) current).getName());
                logger.info("process {} message {}", current.getType(), current.getUid());
            }

            @Override
            protected void failed() {
                Throwable throwable = getException();
                logger.error("process {} message {} failed", current.getType(), current.getUid(), throwable);
            }
        };
    }

    /**
     * 处理“更新窗口信息”消息
     * <p>
     * 1.更新ViewModel
     */
    private Task<Void> processUpdateCounterInfo() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                return null;
            }

            @Override
            protected void succeeded() {
                updateCounterPaneOrShow(((UpdateCounterInfoMessage) current).getNumber(),
                        ((UpdateCounterInfoMessage) current).getName(), null, false);
                logger.info("process {} message {}", current.getType(), current.getUid());
            }

            @Override
            protected void failed() {
                Throwable throwable = getException();
                logger.error("process {} message {} failed", current.getType(), current.getUid(), throwable);
            }
        };
    }

    /**
     * 处理“更新用户信息”消息
     * <p>
     * 1.前置条件 状态=空闲
     * 2.播放声音和更新UI
     * 3.计划5秒返回主屏
     */
    private Task<Void> processUpdateUserInfo() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                checkPreCondition(SERVICE_STATE_IDLE, (String) null);
                //单独更新用户信息一般是没有意义的，故超时仅为5秒
                //通常情况下，超时时间内会收到下一条超时更长的消息
                scheduleTask(returnHome(), USER_SHOW_ONLY_TIMEOUT);
                return null;
            }

            @Override
            protected void succeeded() {
                BaseReceiveMessage.Resource resource = current.getResources().get(ResourceConstant.INDEX_DEFAULT);
                VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                updateUserPaneAndShow(((UpdateUserInfoMessage) current).getNumber(),
                        ((UpdateUserInfoMessage) current).getName(), resource.getImage());
                logger.info("process {} message {}", current.getType(), current.getUid());
            }

            @Override
            protected void failed() {
                Throwable throwable = getException();
                logger.error("process {} message {} failed", current.getType(), current.getUid(), throwable);
            }
        };
    }

    /**
     * 检查消息处理前置条件
     *
     * @param stateExpected 服务状态
     * @param typeExpected  服务类型
     */
    private void checkPreCondition(String stateExpected, String... typeExpected) {
        if (!StringUtils.isEmpty(stateExpected)) {
            String stateActual = PreferencesUtil.get(PreferenceKeyConstant.SERVICE_STATE);
            if (!Objects.equals(stateActual, stateExpected)) {
                logger.error("service state {} does not meet pre condition {}", stateActual, stateExpected);
                throw new ClientException(ClientExceptionEnum.MESSAGE_SERVICE_STATE_MISMATCH);
            }
        }

        if (!ArrayUtils.isEmpty(typeExpected)) {
            String typeActual = PreferencesUtil.get(PreferenceKeyConstant.SERVICE_TYPE);
            if (!StringUtils.equalsAny(typeActual, typeExpected)) {
                logger.error("service type {} does not meet any pre condition {}", typeActual, typeExpected);
                throw new ClientException(ClientExceptionEnum.MESSAGE_SERVICE_TYPE_MISMATCH);
            }
        }
    }

    /**
     * 更新消息处理后置条件
     *
     * @param state 服务状态
     * @param type  服务类型
     */
    private void updatePostCondition(String state, String type) {
        if (StringUtils.isEmpty(state)) {
            PreferencesUtil.remove(PreferenceKeyConstant.SERVICE_STATE);
        } else {
            PreferencesUtil.put(PreferenceKeyConstant.SERVICE_STATE, state);
        }

        if (StringUtils.isEmpty(type)) {
            PreferencesUtil.remove(PreferenceKeyConstant.SERVICE_TYPE);
        } else {
            PreferencesUtil.put(PreferenceKeyConstant.SERVICE_TYPE, type);
        }
    }

    /**
     * 业务取消
     * <p>
     * 1.前置条件 状态=忙碌
     * 2.后置条件 状态=空闲
     */
    private void cancelOngoingService() {
        String type = PreferencesUtil.get(PreferenceKeyConstant.SERVICE_TYPE);
        //指纹登记/辨识：取消SDK操作
        if (StringUtils.equalsAny(type, SERVICE_TYPE_FINGERPRINT_ENROLL, SERVICE_TYPE_FINGERPRINT_IDENTIFY)) {
            IFingerprintService fingerprintService =
                    (IFingerprintService) serviceFactory.getInstance(ServiceFactory.FINGERPRINT);
            fingerprintService.cancel();
        }
    }

    /**
     * 业务超时
     */
    private void timeoutOngoingService() {
        String type = PreferencesUtil.get(PreferenceKeyConstant.SERVICE_TYPE);
        switch (type) {
            case SERVICE_TYPE_GENERAL_BUSINESS:
                CompletableFuture.runAsync(new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        updatePostCondition(SERVICE_STATE_IDLE, null);
                        scheduleTask(returnHome(), RETURN_HOME_TIMEOUT);
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        BaseReceiveMessage.Resource resource =
                                current.getResources().get(ResourceConstant.INDEX_GB_TIMEOUT);
                        VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                        updateMessagePaneAndShow(resource.getImage(),
                                ((GeneralBusinessMessage) current).getExtras().get(ResourceConstant.INDEX_GB_TIMEOUT));
                    }

                    @Override
                    protected void failed() {
                        logger.error("process {} message {} failed when service timeout",
                                current.getType(), current.getUid(), getException());
                    }
                }, executor);
                break;
            case SERVICE_TYPE_FINGERPRINT_ENROLL:
                CompletableFuture.runAsync(new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        IFingerprintService fingerprintService =
                                (IFingerprintService) serviceFactory.getInstance(ServiceFactory.FINGERPRINT);
                        fingerprintService.cancel();

                        updatePostCondition(SERVICE_STATE_IDLE, null);
                        replyFingerprintEnroll(current.getUid(), FINGERPRINT_STATUS_TIMEOUT, null);
                        scheduleTask(returnHome(), RETURN_HOME_TIMEOUT);
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        BaseReceiveMessage.Resource resource =
                                current.getResources().get(ResourceConstant.INDEX_FE_TIMEOUT);
                        VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                        updateMessagePaneAndShow(resource.getImage(),
                                ((FingerprintEnrollMessage) current).getExtras().get(ResourceConstant
                                        .INDEX_FE_TIMEOUT));
                    }

                    @Override
                    protected void failed() {
                        logger.error("process {} message {} failed when service timeout",
                                current.getType(), current.getUid(), getException());
                    }
                }, executor);
                break;
            case SERVICE_TYPE_FINGERPRINT_IDENTIFY:
                CompletableFuture.runAsync(new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        IFingerprintService fingerprintService =
                                (IFingerprintService) serviceFactory.getInstance(ServiceFactory.FINGERPRINT);
                        fingerprintService.cancel();

                        updatePostCondition(SERVICE_STATE_IDLE, null);
                        replyFingerprintIdentify(current.getUid(), FINGERPRINT_STATUS_TIMEOUT, null);
                        scheduleTask(returnHome(), RETURN_HOME_TIMEOUT);
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        BaseReceiveMessage.Resource resource =
                                current.getResources().get(ResourceConstant.INDEX_FI_TIMEOUT);
                        VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                        updateMessagePaneAndShow(resource.getImage(),
                                ((FingerprintIdentifyMessage) current).getExtras().get(ResourceConstant
                                        .INDEX_FI_TIMEOUT));
                    }

                    @Override
                    protected void failed() {
                        logger.error("process {} message {} failed when service timeout",
                                current.getType(), current.getUid(), getException());
                    }
                }, executor);
                break;
        }
    }

    /**
     * 返回主屏
     */
    private Task<Void> returnHome() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                return null;
            }

            @Override
            protected void succeeded() {
                clearUserPaneAndHide();
                clearMessagePaneAndHide();
            }
        };
    }

    /**
     * 计划执行任务
     * 如有前序未完成的任务，先将其取消
     *
     * @param task  任务
     * @param delay 延迟时间
     */
    private void scheduleTask(Runnable task, int delay) {
        //前序任务非空，且非，已取消或已完成
        if (this.future != null && !(this.future.isCancelled() || this.future.isDone())) {
            this.future.cancel(true);
        }
        this.future = executor.schedule(task, delay, TimeUnit.SECONDS);
    }

    /**
     * 回复指纹登记消息
     *
     * @param uid      消息UID
     * @param result   指纹登记结果
     * @param template 指纹模型(可选)
     */
    private void replyFingerprintEnroll(Long uid, String result, String template) {
        FingerprintEnrollReplyMessage message = new FingerprintEnrollReplyMessage();
        switch (result) {
            case FINGERPRINT_STATUS_ERROR:
                message.setUid(uid);
                message.setResult(FINGERPRINT_STATUS_ERROR);
                break;
            case FINGERPRINT_STATUS_TIMEOUT:
                message.setUid(uid);
                message.setResult(FINGERPRINT_STATUS_TIMEOUT);
                break;
            case FINGERPRINT_STATUS_EXTRACT:
                message.setUid(uid);
                message.setResult(FINGERPRINT_STATUS_EXTRACT);
                message.setTemplate(template);
                break;
        }

        IWSService wsService = (IWSService) serviceFactory.getInstance(ServiceFactory.WS);
        wsService.sendMessage(JsonUtil.toJson(message));
    }

    /**
     * 回复指纹辨识消息
     *
     * @param uid      消息UID
     * @param result   指纹辨识结果
     * @param template 指纹模型(可选)
     */
    private void replyFingerprintIdentify(Long uid, String result, String template) {
        FingerprintIdentifyReplyMessage message = new FingerprintIdentifyReplyMessage();
        switch (result) {
            case FINGERPRINT_STATUS_ERROR:
                message.setUid(uid);
                message.setStatus(FINGERPRINT_STATUS_ERROR);
                break;
            case FINGERPRINT_STATUS_TIMEOUT:
                message.setUid(uid);
                message.setStatus(FINGERPRINT_STATUS_TIMEOUT);
                break;
            case FINGERPRINT_STATUS_EXTRACT:
                message.setUid(uid);
                message.setStatus(FINGERPRINT_STATUS_EXTRACT);
                message.setTemplate(template);
                break;
        }

        IWSService wsService = (IWSService) serviceFactory.getInstance(ServiceFactory.WS);
        wsService.sendMessage(JsonUtil.toJson(message));
    }

    //更新窗格ViewModel
    //-----------------------------------------------------------------------

    /**
     * 更新公司信息窗格
     *
     * @param logo LOGO
     * @param name 名称
     */
    private void updateCompanyPane(String logo, String name) {
        bannerModel.setLogo(new Image(!StringUtils.isEmpty(logo) ? logo : ResourceConstant.DEFAULT_COMPANY_LOGO));
        bannerModel.setName(name);
    }

    /**
     * 更新窗口窗格但可选展示
     *
     * @param number 编号
     * @param name   名称
     * @param close  关闭标识
     * @param show   立即展示
     */
    private void updateCounterPaneOrShow(String number, String name, Boolean close, Boolean show) {
        if (!StringUtils.isAllEmpty(number, name)) {
            counterModel.setNumber(number);
            counterModel.setName(name);
        }
        if (close != null) {
            counterModel.setClose(close);
        }
        if (show) {
            paneVisibleModel.setCounterVisible(true);
            paneVisibleModel.setMessageVisible(false);
        }
    }

    /**
     * 更新消息窗格并立即展示
     *
     * @param image   图片
     * @param content 内容
     */
    private void updateMessagePaneAndShow(String image, String content) {
        messageModel.setImage(new Image(!StringUtils.isEmpty(image) ? image : ResourceConstant.DEFAULT_MESSAGE_IMAGE));
        messageModel.setContent(content);
        paneVisibleModel.setCounterVisible(false);
        paneVisibleModel.setMessageVisible(true);
    }

    /**
     * 清除消息窗格并立即隐藏
     */
    private void clearMessagePaneAndHide() {
        messageModel.setImage(new Image(ResourceConstant.DEFAULT_MESSAGE_IMAGE));
        messageModel.setContent(CommonConstant.EMPTY_STRING);
        paneVisibleModel.setCounterVisible(true);
        paneVisibleModel.setMessageVisible(false);
    }

    /**
     * 更新用户窗格并立即展示
     *
     * @param number 编号
     * @param name   姓名
     * @param photo  照片
     */
    private void updateUserPaneAndShow(String number, String name, String photo) {
        userModel.setNumber(number);
        userModel.setName(name);
        userModel.setPhoto(new Image(!StringUtils.isEmpty(photo) ? photo : ResourceConstant.DEFAULT_USER_PHOTO));
        paneVisibleModel.setClockVisible(false);
        paneVisibleModel.setUserVisible(true);
    }

    /**
     * 清除用户窗格并立即隐藏
     */
    private void clearUserPaneAndHide() {
        userModel.setNumber(CommonConstant.EMPTY_STRING);
        userModel.setName(CommonConstant.EMPTY_STRING);
        userModel.setPhoto(new Image(ResourceConstant.DEFAULT_USER_PHOTO));
        paneVisibleModel.setClockVisible(true);
        paneVisibleModel.setUserVisible(false);
    }

    //指纹Service回调方法
    //-----------------------------------------------------------------------
    @Override
    public void onFingerprintScannerError() {
        String type = PreferencesUtil.get(PreferenceKeyConstant.SERVICE_TYPE);
        switch (type) {
            case SERVICE_TYPE_FINGERPRINT_ENROLL:
                CompletableFuture.runAsync(new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        updatePostCondition(SERVICE_STATE_IDLE, null);
                        replyFingerprintEnroll(current.getUid(), FINGERPRINT_STATUS_ERROR, null);
                        scheduleTask(returnHome(), RETURN_HOME_TIMEOUT);
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        BaseReceiveMessage.Resource resource =
                                current.getResources().get(ResourceConstant.INDEX_FE_ERROR);
                        VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                        updateMessagePaneAndShow(resource.getImage(),
                                ((FingerprintEnrollMessage) current).getExtras().get(ResourceConstant.INDEX_FE_ERROR));
                    }

                    @Override
                    protected void failed() {
                        logger.error("process {} message {} failed on fingerprint scanner error callback",
                                current.getType(), current.getUid(), getException());
                    }
                }, executor);
                break;
            case SERVICE_TYPE_FINGERPRINT_IDENTIFY:
                CompletableFuture.runAsync(new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        updatePostCondition(SERVICE_STATE_IDLE, null);
                        replyFingerprintIdentify(current.getUid(), FINGERPRINT_STATUS_ERROR, null);
                        scheduleTask(returnHome(), RETURN_HOME_TIMEOUT);
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        BaseReceiveMessage.Resource resource =
                                current.getResources().get(ResourceConstant.INDEX_FI_ERROR);
                        VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                        updateMessagePaneAndShow(resource.getImage(),
                                ((FingerprintIdentifyMessage) current).getExtras().get(ResourceConstant.INDEX_FI_ERROR));
                    }

                    @Override
                    protected void failed() {
                        logger.error("process {} message {} failed on fingerprint scanner error callback",
                                current.getType(), current.getUid(), getException());
                    }
                }, executor);
                break;
        }
    }

    @Override
    public void onFingerprintEnrollCount(int next) {
        switch (next) {
            case 2:
                CompletableFuture.runAsync(new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        BaseReceiveMessage.Resource resource =
                                current.getResources().get(ResourceConstant.INDEX_FE_2ND);
                        VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                        updateMessagePaneAndShow(resource.getImage(),
                                ((FingerprintIdentifyMessage) current).getExtras().get(ResourceConstant.INDEX_FE_2ND));
                    }

                    @Override
                    protected void failed() {
                        logger.error("process {} message {} failed on fingerprint enroll count callback",
                                current.getType(), current.getUid(), getException());
                    }
                }, executor);
                break;
            case 3:
                CompletableFuture.runAsync(new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        BaseReceiveMessage.Resource resource =
                                current.getResources().get(ResourceConstant.INDEX_FE_3RD);
                        VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                        updateMessagePaneAndShow(resource.getImage(),
                                ((FingerprintIdentifyMessage) current).getExtras().get(ResourceConstant.INDEX_FE_3RD));
                    }

                    @Override
                    protected void failed() {
                        logger.error("process {} message {} failed on fingerprint enroll count callback",
                                current.getType(), current.getUid(), getException());
                    }
                }, executor);
                break;
            case 4:
                CompletableFuture.runAsync(new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        BaseReceiveMessage.Resource resource =
                                current.getResources().get(ResourceConstant.INDEX_FE_4TH);
                        VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                        updateMessagePaneAndShow(resource.getImage(),
                                ((FingerprintIdentifyMessage) current).getExtras().get(ResourceConstant.INDEX_FE_4TH));
                    }

                    @Override
                    protected void failed() {
                        logger.error("process {} message {} failed on fingerprint enroll count callback",
                                current.getType(), current.getUid(), getException());
                    }
                }, executor);
                break;
        }
    }

    @Override
    public void onFingerprintEnrollTemplate(String template) {
        CompletableFuture.runAsync(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                replyFingerprintEnroll(current.getUid(), FINGERPRINT_STATUS_EXTRACT, template);
                return null;
            }

            @Override
            protected void succeeded() {
                BaseReceiveMessage.Resource resource =
                        current.getResources().get(ResourceConstant.INDEX_FE_WAIT);
                VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                updateMessagePaneAndShow(resource.getImage(),
                        ((FingerprintIdentifyMessage) current).getExtras().get(ResourceConstant.INDEX_FE_WAIT));
                logger.error("perform fingerprint enroll template callback for message {}", current.getUid());
            }

            @Override
            protected void failed() {
                logger.error("perform fingerprint enroll template callback for message {} failed",
                        current.getUid(), getException());
            }
        }, executor);
    }

    @Override
    public void onFingerprintIdentifyTemplate(String template) {
        CompletableFuture.runAsync(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                replyFingerprintIdentify(current.getUid(), FINGERPRINT_STATUS_EXTRACT, template);
                return null;
            }

            @Override
            protected void succeeded() {
                BaseReceiveMessage.Resource resource =
                        current.getResources().get(ResourceConstant.INDEX_FI_WAIT);
                VoiceUtil.add(resource.getVoices(), VoiceUtil.HIGH_PRIORITY);
                updateMessagePaneAndShow(resource.getImage(),
                        ((FingerprintIdentifyMessage) current).getExtras().get(ResourceConstant.INDEX_FI_WAIT));
            }

            @Override
            protected void failed() {
                logger.error("process {} message {} failed on fingerprint enroll count callback",
                        current.getType(), current.getUid(), getException());
            }
        }, executor);
    }
}