/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.service;

import cn.com.lx1992.lib.client.constant.CommonConstant;
import cn.com.lx1992.lib.client.constant.RegExpConstant;
import cn.com.lx1992.lib.client.dto.NICAddressDTO;
import cn.com.lx1992.lib.client.util.DateTimeUtil;
import cn.com.lx1992.lib.client.util.JsonUtil;
import cn.com.lx1992.lib.client.util.NativeUtil;
import cn.com.lx1992.lib.client.util.PreferencesUtil;
import cn.edu.xmu.bip.constant.AdminConstant;
import cn.edu.xmu.bip.constant.ApiConstant;
import cn.edu.xmu.bip.constant.PrefConstant;
import cn.edu.xmu.bip.dao.FingerprintLogDAO;
import cn.edu.xmu.bip.dao.MessageLogDAO;
import cn.edu.xmu.bip.dao.ResourceDAO;
import cn.edu.xmu.bip.exception.ClientException;
import cn.edu.xmu.bip.exception.ClientExceptionEnum;
import cn.edu.xmu.bip.param.CounterCreateParam;
import cn.edu.xmu.bip.param.CounterModifyParam;
import cn.edu.xmu.bip.param.CounterQueryBindParam;
import cn.edu.xmu.bip.ui.admin.model.ConfigBackendModel;
import cn.edu.xmu.bip.ui.admin.model.ConfigCounterModel;
import cn.edu.xmu.bip.ui.admin.model.DataBrowsingModel;
import cn.edu.xmu.bip.ui.admin.model.FeedbackModel;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.application.Platform;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 管理工具Service
 *
 * @author luoxin
 * @version 2017-6-26
 */
public class AdminService {
    private final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Inject
    private ApiService apiService;
    @Inject
    private FingerprintLogDAO fingerprintLogDAO;
    @Inject
    private MessageLogDAO messageLogDAO;
    @Inject
    private ResourceDAO resourceDAO;

    @Inject
    private ConfigBackendModel configBackendModel;
    @Inject
    private ConfigCounterModel configCounterModel;
    @Inject
    private DataBrowsingModel dataBrowsingModel;
    @Inject
    private FeedbackModel feedbackModel;

    /**
     * 获取当前后端环境
     */
    public void getCurrentBackend() {
        String api = PreferencesUtil.get(PrefConstant.BACKEND_API);
        String ws = PreferencesUtil.get(PrefConstant.BACKEND_WS);

        if (StringUtils.isEmpty(api) || StringUtils.isEmpty(ws)) {
            logger.warn("backend not configure yet");
            configBackendModel.setApi(CommonConstant.EMPTY_STRING);
            configBackendModel.setWs(CommonConstant.EMPTY_STRING);
            configBackendModel.setHint(AdminConstant.CONFIG_BACKEND_NOT_CONFIGURE);
        } else {
            logger.info("get current backend: api: {}, ws: {}", api, ws);
            configBackendModel.setApi(api);
            configBackendModel.setWs(ws);
            configBackendModel.setHint(CommonConstant.EMPTY_STRING);
        }
    }

    /**
     * 获取预置后端环境
     *
     * @param env 预置环境
     */
    public void getPresetBackend(String env) {
        String api = null, ws = null;
        try (InputStream is = getClass().getResourceAsStream(AdminConstant.CONFIG_BACKEND_PRESET_FILE)) {
            JsonNode root = JsonUtil.toObject(is);
            if (root.has(env)) {
                api = root.get(env).get(AdminConstant.CONFIG_BACKEND_PRESET_KEY_API).asText();
                ws = root.get(env).get(AdminConstant.CONFIG_BACKEND_PRESET_KEY_WS).asText();
            }
        } catch (IOException e) {
            logger.error("get preset backend {} failure", env, e);
        }

        if (StringUtils.isEmpty(api) || StringUtils.isEmpty(ws)) {
            logger.warn("preset env {} unavailable", env);
            configBackendModel.setApi(CommonConstant.EMPTY_STRING);
            configBackendModel.setWs(CommonConstant.EMPTY_STRING);
            configBackendModel.setHint(MessageFormat.format(AdminConstant.CONFIG_BACKEND_PRESET_UNAVAILABLE, env));
        } else {
            logger.info("get preset backend {} successful, api: {}, ws: {}", env, api, ws);
            configBackendModel.setApi(api);
            configBackendModel.setWs(ws);
            configBackendModel.setHint(CommonConstant.EMPTY_STRING);
        }
    }

    /**
     * 保存后端环境
     */
    public void saveBackend() {
        String api = configBackendModel.getApi();
        String ws = configBackendModel.getWs();
        if (validateBackend(api, ws)) {
            PreferencesUtil.put(PrefConstant.BACKEND_API, api);
            PreferencesUtil.put(PrefConstant.BACKEND_WS, ws);

            logger.info("save backend successful, api: {}, ws: {}", api, ws);
            configBackendModel.setHint(AdminConstant.CONFIG_BACKEND_SAVE_SUCCESSFUL);
        }
    }

    /**
     * 校验后端环境
     *
     * @param api API
     * @param ws  WebSocket
     * @return true-校验通过，false-其他
     */
    private boolean validateBackend(String api, String ws) {
        if (!api.matches(AdminConstant.CONFIG_BACKEND_REG_EXP_API)) {
            configBackendModel.setHint(AdminConstant.CONFIG_BACKEND_API_INVALIDATE);
            return false;
        }
        if (!ws.matches(AdminConstant.CONFIG_BACKEND_REG_EXP_WS)) {
            configBackendModel.setHint(AdminConstant.CONFIG_BACKEND_WS_INVALIDATE);
            return false;
        }
        return true;
    }

    /**
     * 获取已绑定的网卡
     */
    public void getBindNic() {
        String nicIndex = PreferencesUtil.get(PrefConstant.BIND_NIC_INDEX);
        if (!StringUtils.isEmpty(nicIndex)) {
            logger.info("get bind nic index {}", nicIndex);
            //从本机网卡列表中选中已绑定的网卡(如果有的话)
            configCounterModel.getNicList().stream()
                    .filter(nicIndexAndName -> nicIndexAndName.startsWith(nicIndex))
                    .findFirst()
                    .ifPresent(nicIndexAndName -> configCounterModel.setNic(nicIndexAndName));
        }
    }

    /**
     * 获取选定网卡的MAC和IP
     */
    public void selectNic() {
        String selectNic = configCounterModel.getNic();
        logger.info("select nic {}", selectNic);

        if (!StringUtils.isEmpty(selectNic)) {
            NICAddressDTO nicAddress = NativeUtil.getNICAddress(Integer.parseInt(selectNic.substring(0, 1)));
            configCounterModel.setMac(nicAddress.getMac());
            configCounterModel.setIp(nicAddress.getIpv4());
            //偏好中保存网卡index，请求后端获取绑定信息
            PreferencesUtil.put(PrefConstant.BIND_NIC_INDEX, selectNic.substring(0, 1));
            queryCounter(nicAddress.getMac(), nicAddress.getIpv4());
        }
    }

    /**
     * 刷新本机网卡列表
     */
    public void updateNicList() {
        //清空已有信息
        configCounterModel.setNumber(CommonConstant.EMPTY_STRING);
        configCounterModel.setName(CommonConstant.EMPTY_STRING);
        configCounterModel.setMac(CommonConstant.EMPTY_STRING);
        configCounterModel.setIp(CommonConstant.EMPTY_STRING);
        configCounterModel.setHint(CommonConstant.EMPTY_STRING);

        List<String> nicIndexAndNames = NativeUtil.getNICInfo().stream()
                .filter(nic -> nic.isUp() && !nic.isLoopback())
                //下拉列表展示格式：index|name
                .map(nic -> nic.getIndex() + "|" + nic.getName())
                .collect(Collectors.toList());
        configCounterModel.getNicList().clear();
        configCounterModel.getNicList().addAll(nicIndexAndNames);
    }

    /**
     * 查询窗口信息
     *
     * @param mac MAC地址
     * @param ip  IP地址
     */
    private void queryCounter(String mac, String ip) {
        if (validateCounter(null, null, mac, ip, false)) {
            CounterQueryBindParam param = new CounterQueryBindParam();
            param.setMac(mac);
            param.setIp(ip);

            logger.info("query counter bind to {}/{}", mac, ip);
            apiService.invokeCounterQueryBind(param)
                    .whenComplete((result, exception) -> {
                        if (exception != null) {
                            Platform.runLater(() -> {
                                configCounterModel.setNumber(CommonConstant.EMPTY_STRING);
                                configCounterModel.setName(CommonConstant.EMPTY_STRING);
                                configCounterModel.setHint(exception.getCause().getMessage());
                            });
                        } else {
                            Platform.runLater(() -> {
                                configCounterModel.setNumber(result.getNumber());
                                configCounterModel.setName(result.getName());
                                configCounterModel.setHint(CommonConstant.EMPTY_STRING);
                            });
                        }
                    });
        }
    }

    /**
     * 提交窗口信息
     */
    public void submitCounter() {
        String number = configCounterModel.getNumber().trim();
        String name = configCounterModel.getName().trim();
        String mac = configCounterModel.getMac();
        String ip = configCounterModel.getIp();
        if (validateCounter(number, name, mac, ip, true)) {
            CounterQueryBindParam param = new CounterQueryBindParam();
            param.setMac(mac);
            param.setIp(ip);

            apiService.invokeCounterQueryBind(param)
                    .whenComplete((result, exception) -> {
                        if (exception != null) {
                            String message = exception.getCause().getMessage();
                            if (message.substring(1).startsWith(ApiConstant.COUNTER_UNBIND_ERROR)) {
                                //调用查询绑定API返回‘未绑定’错误：此窗口未曾绑定过，本次应创建
                                createCounter();
                            } else {
                                Platform.runLater(() -> configCounterModel.setHint(message));
                            }
                        } else {
                            //调用查询绑定API有返回结果：此窗口已绑定过，本次为修改
                            modifyCounter(result.getId());
                        }
                    });
        }
    }

    private void createCounter() {
        CounterCreateParam param = new CounterCreateParam();
        param.setNumber(configCounterModel.getNumber().trim());
        param.setName(configCounterModel.getName().trim());
        param.setMac(configCounterModel.getMac());
        param.setIp(configCounterModel.getIp());

        logger.info("create counter {}/{} and bind to {}/{}",
                param.getNumber(), param.getName(), param.getMac(), param.getIp());
        apiService.invokeCounterCreate(param)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        Platform.runLater(() -> configCounterModel.setHint(exception.getCause().getMessage()));
                    } else {
                        Platform.runLater(() ->
                                configCounterModel.setHint(AdminConstant.CONFIG_COUNTER_SUBMIT_SUCCESSFUL));
                    }
                });
    }

    private void modifyCounter(long id) {
        CounterModifyParam param = new CounterModifyParam();
        param.setId(id);
        param.setNumber(configCounterModel.getNumber().trim());
        param.setName(configCounterModel.getName().trim());
        param.setMac(configCounterModel.getMac());
        param.setIp(configCounterModel.getIp());

        logger.info("modify counter {} to {}/{}", id, param.getNumber(), param.getMac());
        apiService.invokeCounterModify(param)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        Platform.runLater(() -> configCounterModel.setHint(exception.getCause().getMessage()));
                    } else {
                        Platform.runLater(() ->
                                configCounterModel.setHint(AdminConstant.CONFIG_COUNTER_SUBMIT_SUCCESSFUL));
                    }
                });
    }

    /**
     * 校验窗口信息
     *
     * @param number 编号
     * @param name   名称
     * @param mac    MAC地址
     * @param ip     IP地址
     * @param submit true-提交时校验，false-查询时校验
     * @return true-校验通过，false-其他
     */
    private boolean validateCounter(String number, String name, String mac, String ip, boolean submit) {
        if (submit) {
            if (number.length() < 1 || number.length() > 16) {
                configCounterModel.setHint(AdminConstant.CONFIG_COUNTER_NUMBER_INVALIDATE);
                return false;
            }
            if (name.length() < 1 || name.length() > 16) {
                configCounterModel.setHint(AdminConstant.CONFIG_COUNTER_NAME_INVALIDATE);
                return false;
            }
        }
        if (!mac.matches(RegExpConstant.MAC_ADDRESS)) {
            configCounterModel.setHint(AdminConstant.CONFIG_COUNTER_MAC_INVALIDATE);
            return false;
        }
        if (!ip.matches(RegExpConstant.IP_ADDRESS)) {
            configCounterModel.setHint(AdminConstant.CONFIG_COUNTER_IP_INVALIDATE);
            return false;
        }
        return true;
    }

    public void queryFingerprintLog(LocalDate start, LocalDate end) {
        long minTimestamp = DateTimeUtil.convert(DateTimeUtil.getDateTimeAtStartOfDay(start));
        long maxTimestamp = DateTimeUtil.convert(DateTimeUtil.getDateTimeAtEndOfDay(end));
        logger.info("query fingerprint log(s) between ({}, {})", minTimestamp, maxTimestamp);

        CompletableFuture
                .runAsync(() -> {
                    int count = fingerprintLogDAO.count(minTimestamp, maxTimestamp);
                    if (count >= AdminConstant.MAX_QUERY_COUNT) {
                        logger.warn("result set too big({} records)", count);
                        throw new ClientException(ClientExceptionEnum.RESULT_SET_TOO_BIG, count);
                    }
                })
                .thenApplyAsync(nil -> fingerprintLogDAO.select(minTimestamp, maxTimestamp))
                .whenComplete((domains, throwable) -> {
                    if (throwable != null) {
                        logger.error("query fingerprint log failure", throwable);
                        Platform.runLater(() -> dataBrowsingModel.setHint(throwable.getCause().getMessage()));
                    } else {
                        List<DataBrowsingModel.FingerprintLogModel> results = domains.stream()
                                .map(domain -> {
                                    DataBrowsingModel.FingerprintLogModel model = new DataBrowsingModel
                                            .FingerprintLogModel();
                                    model.setId(domain.getId());
                                    model.setType(domain.getType());
                                    model.setContent(domain.getContent());
                                    model.setTimestamp(domain.getTimestamp());
                                    return model;
                                })
                                .collect(Collectors.toList());

                        logger.info("query {} fingerprint log(s) successful", results.size());
                        Platform.runLater(() -> {
                            dataBrowsingModel.setHint(
                                    MessageFormat.format(AdminConstant.DATA_BROWSING_QUERY_SUCCESSFUL, results.size()));
                            dataBrowsingModel.getFingerprintLogs().clear();
                            dataBrowsingModel.getFingerprintLogs().addAll(results);
                        });
                    }
                });
    }

    public void queryMessageLog(LocalDate start, LocalDate end) {
        long minTimestamp = DateTimeUtil.convert(DateTimeUtil.getDateTimeAtStartOfDay(start));
        long maxTimestamp = DateTimeUtil.convert(DateTimeUtil.getDateTimeAtEndOfDay(end));
        logger.info("query message log between ({}, {})", minTimestamp, maxTimestamp);

        CompletableFuture
                .runAsync(() -> {
                    int count = messageLogDAO.count(minTimestamp, maxTimestamp);
                    if (count >= AdminConstant.MAX_QUERY_COUNT) {
                        logger.warn("result set too big({} records)", count);
                        throw new ClientException(ClientExceptionEnum.RESULT_SET_TOO_BIG, count);
                    }
                })
                .thenApplyAsync(nil -> messageLogDAO.select(minTimestamp, maxTimestamp))
                .whenComplete((domains, throwable) -> {
                    if (throwable != null) {
                        logger.error("query message log failure", throwable);
                        Platform.runLater(() -> dataBrowsingModel.setHint(throwable.getCause().getMessage()));
                    } else {
                        List<DataBrowsingModel.MessageLogModel> results = domains.stream()
                                .map(domain -> {
                                    DataBrowsingModel.MessageLogModel model = new DataBrowsingModel.MessageLogModel();
                                    model.setId(domain.getId());
                                    model.setType(domain.getType());
                                    model.setBody(domain.getBody());
                                    model.setTimestamp(domain.getTimestamp());
                                    return model;
                                })
                                .collect(Collectors.toList());

                        logger.info("query {} message log(s) successful", results.size());
                        Platform.runLater(() -> {
                            dataBrowsingModel.setHint(
                                    MessageFormat.format(AdminConstant.DATA_BROWSING_QUERY_SUCCESSFUL, results.size()));
                            dataBrowsingModel.getMessageLogs().clear();
                            dataBrowsingModel.getMessageLogs().addAll(results);
                        });
                    }
                });
    }

    public void queryResource(LocalDate start, LocalDate end) {
        long minTimestamp = DateTimeUtil.convert(DateTimeUtil.getDateTimeAtStartOfDay(start));
        long maxTimestamp = DateTimeUtil.convert(DateTimeUtil.getDateTimeAtEndOfDay(end));
        logger.info("query resource between ({}, {})", minTimestamp, maxTimestamp);

        CompletableFuture
                .runAsync(() -> {
                    int count = resourceDAO.count(minTimestamp, maxTimestamp);
                    if (count >= AdminConstant.MAX_QUERY_COUNT) {
                        logger.warn("result set too big({} records)", count);
                        throw new ClientException(ClientExceptionEnum.RESULT_SET_TOO_BIG, count);
                    }
                })
                .thenApplyAsync(nil -> resourceDAO.select(minTimestamp, maxTimestamp))
                .whenComplete((domains, throwable) -> {
                    if (throwable != null) {
                        logger.error("query resource failure", throwable);
                        Platform.runLater(() -> dataBrowsingModel.setHint(throwable.getCause().getMessage()));
                    } else {
                        List<DataBrowsingModel.ResourceModel> results = domains.stream()
                                .map(domain -> {
                                    DataBrowsingModel.ResourceModel model = new DataBrowsingModel.ResourceModel();
                                    model.setId(domain.getId());
                                    model.setType(domain.getType());
                                    model.setUrl(domain.getUrl());
                                    model.setPath(domain.getPath());
                                    model.setMd5(domain.getMd5());
                                    model.setTimestamp(domain.getTimestamp());
                                    model.setStatus(domain.getStatus());
                                    return model;
                                })
                                .collect(Collectors.toList());

                        logger.info("query {} resource(s) successful", results.size());
                        Platform.runLater(() -> {
                            dataBrowsingModel.setHint(
                                    MessageFormat.format(AdminConstant.DATA_BROWSING_QUERY_SUCCESSFUL, results.size()));
                            dataBrowsingModel.getResources().clear();
                            dataBrowsingModel.getResources().addAll(results);
                        });
                    }
                });
    }


}
