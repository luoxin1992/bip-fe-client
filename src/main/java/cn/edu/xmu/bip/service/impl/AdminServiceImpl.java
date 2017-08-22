/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.service.impl;

import cn.com.lx1992.lib.client.constant.RegExpConstant;
import cn.com.lx1992.lib.client.dto.NicAddressDTO;
import cn.com.lx1992.lib.client.dto.NicInfoDTO;
import cn.com.lx1992.lib.client.util.DateTimeUtil;
import cn.com.lx1992.lib.client.util.JsonUtil;
import cn.com.lx1992.lib.client.util.NativeUtil;
import cn.com.lx1992.lib.client.util.PreferencesUtil;
import cn.edu.xmu.bip.constant.PreferenceKeyConstant;
import cn.edu.xmu.bip.dao.IFingerprintDAO;
import cn.edu.xmu.bip.dao.IMessageDAO;
import cn.edu.xmu.bip.dao.IResourceDAO;
import cn.edu.xmu.bip.dao.factory.DAOFactory;
import cn.edu.xmu.bip.domain.FingerprintDO;
import cn.edu.xmu.bip.domain.MessageDO;
import cn.edu.xmu.bip.domain.ResourceDO;
import cn.edu.xmu.bip.exception.ClientException;
import cn.edu.xmu.bip.exception.ClientExceptionEnum;
import cn.edu.xmu.bip.exception.ServerException;
import cn.edu.xmu.bip.param.CounterCreateParam;
import cn.edu.xmu.bip.param.CounterModifyParam;
import cn.edu.xmu.bip.param.CounterQueryBindParam;
import cn.edu.xmu.bip.result.CounterQueryBindResult;
import cn.edu.xmu.bip.service.IAPIService;
import cn.edu.xmu.bip.service.IAdminService;
import cn.edu.xmu.bip.service.factory.ServiceFactory;
import cn.edu.xmu.bip.ui.admin.model.ConfigBackendModel;
import cn.edu.xmu.bip.ui.admin.model.ConfigCounterModel;
import cn.edu.xmu.bip.ui.admin.model.DataBrowsingModel;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.concurrent.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AdminServiceImpl implements IAdminService {
    private static final String CONFIG_BACKEND_PRESET_FILE = "/file/preset-backend.json";
    private static final String CONFIG_BACKEND_KEY_API = "api";
    private static final String CONFIG_BACKEND_KEY_WS = "ws";
    private static final int CONFIG_COUNTER_UNBIND_ERROR_CODE = 20408;
    private static final int DATA_BROWSING_MAX_QUERY_COUNT = 1000;

    private final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Inject
    private ServiceFactory serviceFactory;
    @Inject
    private DAOFactory daoFactory;

    @Inject
    private ConfigBackendModel configBackendModel;
    @Inject
    private ConfigCounterModel configCounterModel;
    @Inject
    private DataBrowsingModel dataBrowsingModel;

    @Override
    public void getCurrentBackend() {
        CompletableFuture.runAsync(new Task<Map<String, String>>() {
            @Override
            protected Map<String, String> call() throws Exception {
                String api = PreferencesUtil.get(PreferenceKeyConstant.CONFIG_BACKEND_API);
                String ws = PreferencesUtil.get(PreferenceKeyConstant.CONFIG_BACKEND_WS);

                if (StringUtils.isEmpty(api) || StringUtils.isEmpty(ws)) {
                    logger.warn("backend url not configured");
                    throw new ClientException(ClientExceptionEnum.ADMIN_CONFIG_BACKEND_NOT_CONFIGURE);
                }

                Map<String, String> retMap = new HashMap<>();
                retMap.put(CONFIG_BACKEND_KEY_API, api);
                retMap.put(CONFIG_BACKEND_KEY_WS, ws);
                return retMap;
            }

            @Override
            protected void succeeded() {
                configBackendModel.setApi(getValue().get(CONFIG_BACKEND_KEY_API));
                configBackendModel.setWs(getValue().get(CONFIG_BACKEND_KEY_WS));
                configBackendModel.setMessage(null);
            }

            @Override
            protected void failed() {
                configBackendModel.setApi(null);
                configBackendModel.setWs(null);
                configBackendModel.setMessage(getException().getMessage());
            }
        });
    }

    @Override
    public void getPresetBackend(String preset) {
        CompletableFuture.runAsync(new Task<Map<String, String>>() {
            @Override
            protected Map<String, String> call() throws Exception {
                try (InputStream is = getClass().getResourceAsStream(CONFIG_BACKEND_PRESET_FILE)) {
                    JsonNode root = JsonUtil.toObject(is);
                    if (!root.has(preset)) {
                        logger.warn("backend preset {} unavailable", preset);
                        throw new ClientException(ClientExceptionEnum.ADMIN_CONFIG_BACKEND_PRESET_UNAVAILABLE, preset);
                    }

                    String api = root.get(preset).get(CONFIG_BACKEND_KEY_API).asText();
                    String ws = root.get(preset).get(CONFIG_BACKEND_KEY_WS).asText();

                    Map<String, String> retMap = new HashMap<>();
                    retMap.put(CONFIG_BACKEND_KEY_API, api);
                    retMap.put(CONFIG_BACKEND_KEY_WS, ws);
                    return retMap;
                } catch (IOException e) {
                    logger.error("get backend preset {} failed", preset, e);
                    throw new ClientException(ClientExceptionEnum.ADMIN_CONFIG_BACKEND_PRESET_ERROR);
                }
            }

            @Override
            protected void succeeded() {
                configBackendModel.setApi(getValue().get(CONFIG_BACKEND_KEY_API));
                configBackendModel.setWs(getValue().get(CONFIG_BACKEND_KEY_WS));
                configBackendModel.setMessage(null);
            }

            @Override
            protected void failed() {
                configBackendModel.setApi(null);
                configBackendModel.setWs(null);
                configBackendModel.setMessage(getException().getMessage());
            }
        });
    }

    @Override
    public void saveBackend() {
        CompletableFuture.runAsync(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String api = configBackendModel.getApi();
                String ws = configBackendModel.getWs();

                validateConfigBackendParam(api, ws);
                PreferencesUtil.put(PreferenceKeyConstant.CONFIG_BACKEND_API, api);
                PreferencesUtil.put(PreferenceKeyConstant.CONFIG_BACKEND_WS, ws);
                return null;
            }

            @Override
            protected void succeeded() {
                String message = "保存成功，重新启动程序后生效.";
                configBackendModel.setMessage(message);
            }

            @Override
            protected void failed() {
                String message = getException().getMessage();
                configBackendModel.setMessage(message);
            }
        });
    }

    /**
     * 后端环境配置参数校验
     *
     * @param api API
     * @param ws  WebSocket
     */
    private void validateConfigBackendParam(String api, String ws) {
        if (!api.matches(RegExpConstant.HTTP_URL_PREFIX)) {
            logger.warn("api url is malformed");
            throw new ClientException(ClientExceptionEnum.ADMIN_CONFIG_BACKEND_INVALID_PARAM_API);
        }
        if (!ws.matches(RegExpConstant.WEB_SOCKET_URL_PREFIX)) {
            logger.warn("web socket url is malformed");
            throw new ClientException(ClientExceptionEnum.ADMIN_CONFIG_BACKEND_INVALID_PARAM_WS);
        }
    }

    public void selectNic() {
        CompletableFuture.runAsync(new Task<NicAddressDTO>() {
            @Override
            protected void scheduled() {
                String nic = configCounterModel.getNicCurrent();
                if (StringUtils.isEmpty(nic)) {
                    cancel();
                }
            }

            @Override
            protected NicAddressDTO call() throws Exception {
                String nic = configCounterModel.getNicCurrent();
                int index = Integer.parseInt(nic.substring(0, nic.indexOf('|')));

                //保存网卡index到偏好中
                PreferencesUtil.put(PreferenceKeyConstant.CONFIG_BIND_NIC_INDEX, index);
                return NativeUtil.getNicAddress(index);
            }

            @Override
            protected void succeeded() {
                //先清除原有网卡信息 防止部分更新时触发查询窗口绑定事件
                configCounterModel.setMac(null);
                configCounterModel.setIp(null);
                configCounterModel.setMac(getValue().getMac());
                configCounterModel.setIp(getValue().getIpv4());
                configCounterModel.setMessage(null);
            }

            @Override
            protected void failed() {
                String message = getException().getMessage();
                configCounterModel.setMessage(message);
            }
        });
    }

    @Override
    public void refreshNicList() {
        CompletableFuture.runAsync(new Task<Map<String, Boolean>>() {
            @Override
            protected Map<String, Boolean> call() throws Exception {
                //本机全部网卡
                List<NicInfoDTO> allNic = NativeUtil.getNicInfo().stream()
                        //过滤禁用的网卡以及本地环回
                        .filter(nic -> nic.isUp() && !nic.isLoopback())
                        .collect(Collectors.toList());
                //当前绑定网卡索引号
                String bindNic = PreferencesUtil.get(PreferenceKeyConstant.CONFIG_BIND_NIC_INDEX);

                return allNic.stream().collect(Collectors.toMap(
                        //展示格式：索引号|名称
                        nic -> nic.getIndex() + "|" + nic.getName(),
                        nic -> bindNic != null && Objects.equals(nic.getIndex(), Integer.parseInt(bindNic))));
            }

            @Override
            protected void succeeded() {
                //清空已有信息
                configCounterModel.setNumber(null);
                configCounterModel.setName(null);
                configCounterModel.setMac(null);
                configCounterModel.setIp(null);
                configCounterModel.setMessage(null);

                configCounterModel.getNicList().clear();
                configCounterModel.getNicList().addAll(getValue().keySet());

                //默认选中已绑定的网卡(如果有的话)
                getValue().entrySet().stream()
                        .filter(Map.Entry::getValue)
                        .findFirst()
                        .ifPresent(entry -> configCounterModel.setNicCurrent(entry.getKey()));
            }

            @Override
            protected void failed() {
                String message = getException().getMessage();
                configCounterModel.setMessage(message);
            }
        });
    }

    @Override
    public void queryCounter() {
        CompletableFuture.runAsync(new Task<CounterQueryBindResult>() {
            @Override
            protected void scheduled() {
                String mac = configCounterModel.getMac();
                String ip = configCounterModel.getIp();
                //如果MAC地址或IP地址任一为空 则取消执行
                if (StringUtils.isEmpty(mac) || StringUtils.isEmpty(ip)) {
                    cancel();
                }
            }

            @Override
            protected CounterQueryBindResult call() throws Exception {
                String mac = configCounterModel.getMac();
                String ip = configCounterModel.getIp();

                validateConfigCounterParam(mac, ip);
                return queryCounterBind(mac, ip);
            }

            @Override
            protected void succeeded() {
                configCounterModel.setNumber(getValue().getNumber());
                configCounterModel.setName(getValue().getName());
                configCounterModel.setMessage(null);
            }

            @Override
            protected void failed() {
                String message = getException().getMessage();
                configCounterModel.setMessage(message);
            }
        });
    }

    @Override
    public void submitCounter() {
        CompletableFuture.runAsync(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String number = configCounterModel.getNumber().trim();
                String name = configCounterModel.getName().trim();
                String mac = configCounterModel.getMac();
                String ip = configCounterModel.getIp();

                validateConfigCounterParam(number, name, mac, ip);
                try {
                    CounterQueryBindResult result = queryCounterBind(mac, ip);
                    //调用查询绑定API有返回结果：此窗口已绑定过，本次为修改
                    modifyCounter(result.getId(), number, name, mac, ip);
                } catch (ServerException e) {
                    if (e.getCode() == CONFIG_COUNTER_UNBIND_ERROR_CODE) {
                        //调用查询绑定API返回‘未绑定’错误：此窗口未曾绑定过，本次应创建
                        createCounter(number, name, mac, ip);
                    } else {
                        throw e;
                    }
                }
                return null;
            }

            @Override
            protected void succeeded() {
                String message = "提交成功.";
                configCounterModel.setMessage(message);
            }

            @Override
            protected void failed() {
                String message = getException().getMessage();
                configCounterModel.setMessage(message);
            }
        });
    }

    /**
     * 窗口信息配置参数校验
     *
     * @param number 编号
     * @param name   名称
     * @param mac    MAC地址
     * @param ip     IP地址
     */
    private void validateConfigCounterParam(String number, String name, String mac, String ip) {
        if (number.length() < 1 || number.length() > 16) {
            logger.warn("counter number length is malformed");
            throw new ClientException(ClientExceptionEnum.ADMIN_CONFIG_COUNTER_INVALID_PARAM_NUMBER);
        }
        if (name.length() < 1 || name.length() > 16) {
            logger.warn("counter name length is malformed");
            throw new ClientException(ClientExceptionEnum.ADMIN_CONFIG_COUNTER_INVALID_PARAM_NAME);
        }
        if (!mac.matches(RegExpConstant.MAC_ADDRESS)) {
            logger.warn("counter mac address is malformed");
            throw new ClientException(ClientExceptionEnum.ADMIN_CONFIG_COUNTER_INVALID_PARAM_MAC);
        }
        if (!ip.matches(RegExpConstant.IP_ADDRESS)) {
            logger.warn("counter ip address is malformed");
            throw new ClientException(ClientExceptionEnum.ADMIN_CONFIG_COUNTER_INVALID_PARAM_IP);
        }
    }

    /**
     * 窗口信息配置参数校验
     *
     * @param mac MAC地址
     * @param ip  IP地址
     */
    private void validateConfigCounterParam(String mac, String ip) {
        if (!mac.matches(RegExpConstant.MAC_ADDRESS)) {
            logger.warn("counter mac address is malformed");
            throw new ClientException(ClientExceptionEnum.ADMIN_CONFIG_COUNTER_INVALID_PARAM_MAC);
        }
        if (!ip.matches(RegExpConstant.IP_ADDRESS)) {
            logger.warn("counter ip address is malformed");
            throw new ClientException(ClientExceptionEnum.ADMIN_CONFIG_COUNTER_INVALID_PARAM_IP);
        }
    }

    /**
     * 查询窗口绑定
     *
     * @param mac MAC地址
     * @param ip  IP地址
     * @return 查询结果
     */
    private CounterQueryBindResult queryCounterBind(String mac, String ip) {
        CounterQueryBindParam param = new CounterQueryBindParam();
        param.setMac(mac);
        param.setIp(ip);

        IAPIService apiService = (IAPIService) serviceFactory.getInstance(ServiceFactory.API);
        return apiService.invokeCounterQueryBind(param);
    }

    /**
     * 新增窗口
     *
     * @param number 编号
     * @param name   名称
     * @param mac    MAC地址
     * @param ip     IP地址
     */
    private void createCounter(String number, String name, String mac, String ip) {
        CounterCreateParam param = new CounterCreateParam();
        param.setNumber(number);
        param.setName(name);
        param.setMac(mac);
        param.setIp(ip);

        IAPIService apiService = (IAPIService) serviceFactory.getInstance(ServiceFactory.API);
        apiService.invokeCounterCreate(param);
    }

    /**
     * 编辑窗口
     *
     * @param id     ID
     * @param number 编号
     * @param name   名称
     * @param mac    MAC地址
     * @param ip     IP地址
     */
    private void modifyCounter(long id, String number, String name, String mac, String ip) {
        CounterModifyParam param = new CounterModifyParam();
        param.setId(id);
        param.setNumber(number);
        param.setName(name);
        param.setMac(mac);
        param.setIp(ip);

        IAPIService apiService = (IAPIService) serviceFactory.getInstance(ServiceFactory.API);
        apiService.invokeCounterModify(param);
    }

    @Override
    public void queryFingerprint() {
        CompletableFuture.runAsync(new Task<List<FingerprintDO>>() {
            @Override
            protected List<FingerprintDO> call() throws Exception {
                LocalDateTime start = DateTimeUtil.getDateTimeAtStartOfDay(dataBrowsingModel.getStart());
                LocalDateTime end = DateTimeUtil.getDateTimeAtEndOfDay(dataBrowsingModel.getEnd());

                validateDataBrowsingParam(start, end);
                return queryFingerprint(DateTimeUtil.convert(start), DateTimeUtil.convert(end));
            }

            @Override
            protected void succeeded() {
                List<DataBrowsingModel.FingerprintModel> models = getValue().stream()
                        .map(domain -> {
                            DataBrowsingModel.FingerprintModel model = new DataBrowsingModel.FingerprintModel();
                            model.setId(domain.getId());
                            model.setEvent(domain.getEvent());
                            model.setExtra(domain.getExtra());
                            model.setTimestamp(domain.getTimestamp());
                            return model;
                        })
                        .collect(Collectors.toList());
                dataBrowsingModel.getFingerprints().clear();
                dataBrowsingModel.getFingerprints().addAll(models);

                String message = MessageFormat.format("查询结果共{0}行.", models.size());
                dataBrowsingModel.setMessage(message);
            }

            @Override
            protected void failed() {
                String message = getException().getMessage();
                dataBrowsingModel.setMessage(message);
            }
        });
    }

    /**
     * 查询指纹(仪)(日志)表
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 查询结果
     */
    private List<FingerprintDO> queryFingerprint(long start, long end) {
        IFingerprintDAO fingerprintDAO = (IFingerprintDAO) daoFactory.getInstance(DAOFactory.FINGERPRINT);
        logger.info("query fingerprint scanner log(s) between {} and {}", start, end);

        checkResultLength(fingerprintDAO.count(start, end));
        return fingerprintDAO.query(start, end);
    }

    @Override
    public void queryMessage() {
        CompletableFuture.runAsync(new Task<List<MessageDO>>() {
            @Override
            protected List<MessageDO> call() throws Exception {
                LocalDateTime start = DateTimeUtil.getDateTimeAtStartOfDay(dataBrowsingModel.getStart());
                LocalDateTime end = DateTimeUtil.getDateTimeAtEndOfDay(dataBrowsingModel.getEnd());

                validateDataBrowsingParam(start, end);
                return queryMessage(DateTimeUtil.convert(start), DateTimeUtil.convert(end));
            }

            @Override
            protected void succeeded() {
                List<DataBrowsingModel.MessageModel> models = getValue().stream()
                        .map(domain -> {
                            DataBrowsingModel.MessageModel model = new DataBrowsingModel.MessageModel();
                            model.setId(domain.getId());
                            model.setUid(domain.getUid());
                            model.setType(domain.getType());
                            model.setBody(domain.getBody());
                            model.setTimestamp(domain.getTimestamp());
                            return model;
                        })
                        .collect(Collectors.toList());
                dataBrowsingModel.getMessages().clear();
                dataBrowsingModel.getMessages().addAll(models);

                String message = MessageFormat.format("查询结果共{0}行.", models.size());
                dataBrowsingModel.setMessage(message);
            }

            @Override
            protected void failed() {
                String message = getException().getMessage();
                dataBrowsingModel.setMessage(message);
            }
        });
    }

    /**
     * 查询消息(日志)表
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 查询结果
     */
    private List<MessageDO> queryMessage(long start, long end) {
        IMessageDAO messageDAO = (IMessageDAO) daoFactory.getInstance(DAOFactory.MESSAGE);
        logger.info("query message log(s) between {} and {}", start, end);

        checkResultLength(messageDAO.count(start, end));
        return messageDAO.query(start, end);
    }

    @Override
    public void queryResource() {
        CompletableFuture.runAsync(new Task<List<ResourceDO>>() {
            @Override
            protected List<ResourceDO> call() throws Exception {
                LocalDateTime start = DateTimeUtil.getDateTimeAtStartOfDay(dataBrowsingModel.getStart());
                LocalDateTime end = DateTimeUtil.getDateTimeAtEndOfDay(dataBrowsingModel.getEnd());

                validateDataBrowsingParam(start, end);
                return queryResource(DateTimeUtil.convert(start), DateTimeUtil.convert(end));
            }

            @Override
            protected void succeeded() {
                List<DataBrowsingModel.ResourceModel> models = getValue().stream()
                        .map(domain -> {
                            DataBrowsingModel.ResourceModel model = new DataBrowsingModel.ResourceModel();
                            model.setId(domain.getId());
                            model.setType(domain.getType());
                            model.setUrl(domain.getUrl());
                            model.setPath(domain.getPath());
                            model.setLength(domain.getLength());
                            model.setModify(domain.getModify());
                            model.setTimestamp(domain.getTimestamp());
                            return model;
                        })
                        .collect(Collectors.toList());
                dataBrowsingModel.getResources().clear();
                dataBrowsingModel.getResources().addAll(models);

                String message = MessageFormat.format("查询结果共{0}行.", models.size());
                dataBrowsingModel.setMessage(message);
            }

            @Override
            protected void failed() {
                String message = getException().getMessage();
                dataBrowsingModel.setMessage(message);
            }
        });
    }

    /**
     * 查询资源表
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 查询结果
     */
    private List<ResourceDO> queryResource(long start, long end) {
        IResourceDAO resourceDAO = (IResourceDAO) daoFactory.getInstance(DAOFactory.RESOURCE);
        logger.info("query resource(s) between {} and {}", start, end);

        checkResultLength(resourceDAO.count(start, end));
        return resourceDAO.query(start, end);
    }

    /**
     * 检查查询参数(时间戳)
     *
     * @param start 结束时间
     * @param end   开始时间
     */
    private void validateDataBrowsingParam(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            logger.warn("start date or end date is null");
            throw new ClientException(ClientExceptionEnum.ADMIN_DATA_BROWSING_DATE_NULL);
        }
        if (start.isAfter(end)) {
            logger.warn("start date is after end date");
            throw new ClientException(ClientExceptionEnum.ADMIN_DATA_BROWSING_DATE_INVALID);
        }
    }

    /**
     * 检查结果长度
     *
     * @param length 结果长度
     */
    private void checkResultLength(int length) {
        if (length > DATA_BROWSING_MAX_QUERY_COUNT) {
            logger.warn("query result set length {} is too long", length);
            throw new ClientException(ClientExceptionEnum.ADMIN_DATA_BROWSING_RESULT_SET_TOO_LONG, length);
        }
    }
}
