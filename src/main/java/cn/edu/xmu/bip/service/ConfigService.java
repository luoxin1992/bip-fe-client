/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.service;

import cn.com.lx1992.lib.client.constant.RegExpConstant;
import cn.com.lx1992.lib.client.util.JsonUtil;
import cn.com.lx1992.lib.client.util.PreferencesUtil;
import cn.edu.xmu.bip.constant.AdminConstant;
import cn.edu.xmu.bip.constant.ApiConstant;
import cn.edu.xmu.bip.param.CounterCreateParam;
import cn.edu.xmu.bip.param.CounterModifyParam;
import cn.edu.xmu.bip.param.CounterQueryBindParam;
import cn.edu.xmu.bip.result.CounterQuerySimpleResult;
import cn.edu.xmu.bip.constant.CommonConstant;
import cn.edu.xmu.bip.ui.admin.model.BackendModel;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * 参数配置Service
 *
 * @author luoxin
 * @version 2017-6-26
 */
public class ConfigService {
    private final Logger logger = LoggerFactory.getLogger(ConfigService.class);

    @Inject
    private ApiService apiService;

    /**
     * 获取后端环境配置
     *
     * @param env 环境名称
     * @return 后端环境配置
     */
    public BackendModel getBackend(String env) {
        //若未指定要查询的env，则从偏好中取当前env；若当前env不存在，直接返回自定义(默认)env
        if (StringUtils.isEmpty(env)) {
            env = PreferencesUtil.get(AdminConstant.BACKEND_ENV_PREF);
            if (StringUtils.isEmpty(env)) {
                env = AdminConstant.BACKEND_ENV_CUSTOM;
                logger.warn("backend env not configure, fallback to CUSTOM");
            }
        }

        //3个预置的env从文件中获取，自定义env从偏好中获取
        String api = null, ws = null;
        switch (env) {
            case AdminConstant.BACKEND_ENV_RD:
            case AdminConstant.BACKEND_ENV_QA:
            case AdminConstant.BACKEND_ENV_PROD:
                try (InputStream is = getClass().getResourceAsStream(AdminConstant.PRESET_BACKEND_FILE)) {
                    JsonNode root = JsonUtil.toObject(is);
                    api = root.get(env).get(AdminConstant.PRESET_BACKEND_API_KEY).asText();
                    ws = root.get(env).get(AdminConstant.PRESET_BACKEND_WS_KEY).asText();
                } catch (IOException e) {
                    logger.error("get backend env failed", e);
                }
                break;
            default:
                api = PreferencesUtil.get(AdminConstant.BACKEND_CUSTOM_API_PREF);
                ws = PreferencesUtil.get(AdminConstant.BACKEND_CUSTOM_WS_PREF);
                break;
        }

        logger.info("get backend env {} successful. api: {}, ws: {}", env, api, ws);
        BackendModel model = new BackendModel();
        model.setEnv(env);
        model.setApi(StringUtils.isEmpty(api) ? CommonConstant.EMPTY_STRING : api);
        model.setWs(StringUtils.isEmpty(ws) ? CommonConstant.EMPTY_STRING : ws);
        return model;
    }

    /**
     * 保存后端环境配置
     *
     * @param env 环境
     * @param api API
     * @param ws  WebSocket
     * @return 保存结果
     */
    public String saveBackend(String env, String api, String ws) {
        String validateResult = validateBackend(api, ws);
        if (!StringUtils.isEmpty(validateResult)) {
            return validateResult;
        }

        PreferencesUtil.put(AdminConstant.BACKEND_ENV_PREF, env);
        PreferencesUtil.put(AdminConstant.BACKEND_CURRENT_API_PREF, api);
        PreferencesUtil.put(AdminConstant.BACKEND_CURRENT_WS_PREF, ws);
        //自定义env保存额外的偏好
        if (AdminConstant.BACKEND_ENV_CUSTOM.equals(env)) {
            PreferencesUtil.put(AdminConstant.BACKEND_CUSTOM_API_PREF, api);
            PreferencesUtil.put(AdminConstant.BACKEND_CUSTOM_WS_PREF, ws);
        }

        logger.info("save backend env {} successful. api: {}, ws: {}", env, api, ws);
        return AdminConstant.BACKEND_SAVE_SUCCESSFUL;
    }

    /**
     * 校验后端环境配置
     *
     * @param api API
     * @param ws  WebSocket
     * @return null-校验通过，non-null-错误提示
     */
    private String validateBackend(String api, String ws) {
        if (!api.matches(AdminConstant.BACKEND_API_REG_EXP)) {
            return AdminConstant.BACKEND_API_INVALIDATE;
        }
        if (!ws.matches(AdminConstant.BACKEND_WS_REG_EXP)) {
            return AdminConstant.BACKEND_WS_INVALIDATE;
        }
        return null;
    }

    /**
     * 查询窗口信息
     *
     * @param mac       MAC地址
     * @param ip        IP地址
     * @param onSuccess 成功回调
     * @param onFailure 失败回调
     */
    public void queryCounter(String mac, String ip,
                             Consumer<CounterQuerySimpleResult> onSuccess, Consumer<String> onFailure) {
        String validateResult = validateCounter(null, null, mac, ip, false);
        if (!StringUtils.isEmpty(validateResult)) {
            onFailure.accept(validateResult);
            return;
        }

        CounterQueryBindParam param = new CounterQueryBindParam();
        param.setMac(mac);
        param.setIp(ip);

        logger.info("query counter bind to mac {} and ip {}", mac, ip);
        apiService.invokeCounterQueryBind(param,
                onSuccess, status -> onFailure.accept(status.getValue()), onFailure);
    }

    /**
     * 提交窗口信息
     *
     * @param number    编号
     * @param name      名称
     * @param mac       MAC地址
     * @param ip        IP地址
     * @param onSuccess 成功回调
     * @param onFailure 失败回调
     */
    public void submitCounter(String number, String name, String mac, String ip,
                              Consumer<Void> onSuccess, Consumer<String> onFailure) {
        String validateResult = validateCounter(number, name, mac, ip, true);
        if (!StringUtils.isEmpty(validateResult)) {
            onFailure.accept(validateResult);
            return;
        }

        CounterQueryBindParam bindParam = new CounterQueryBindParam();
        bindParam.setMac(mac);
        bindParam.setIp(ip);

        apiService.invokeCounterQueryBind(bindParam,
                result -> {
                    //调用查询绑定API有返回结果：此窗口已绑定过，本次为修改
                    CounterModifyParam modifyParam = new CounterModifyParam();
                    modifyParam.setId(result.getId());
                    modifyParam.setNumber(number);
                    modifyParam.setName(name);
                    modifyParam.setMac(mac);
                    modifyParam.setIp(ip);

                    logger.info("modify counter {} to {}|{}", result.getId(), number, name);
                    apiService.invokeCounterModify(modifyParam, onSuccess, onFailure);
                }, status -> {
                    //调用查询绑定API返回‘未绑定’错误：此窗口未曾绑定过，本次应创建
                    if (status.getKey() == ApiConstant.COUNTER_UNBIND_ERROR) {
                        CounterCreateParam createParam = new CounterCreateParam();
                        createParam.setNumber(number);
                        createParam.setName(name);
                        createParam.setMac(mac);
                        createParam.setIp(ip);

                        logger.info("create counter {}|{} and bind to {]|{}", number, name, mac, ip);
                        apiService.invokeCounterCreate(createParam, onSuccess, onFailure);
                    } else {
                        onFailure.accept(status.getValue());
                    }
                }, onFailure);
    }

    /**
     * 校验窗口信息
     *
     * @param number   编号
     * @param name     名称
     * @param mac      MAC地址
     * @param ip       IP地址
     * @param isSubmit true-提交时校验，false-查询时校验
     * @return null-校验通过，non-null-错误提示
     */
    private String validateCounter(String number, String name, String mac, String ip, boolean isSubmit) {
        if (isSubmit) {
            if (number.length() < 1 || number.length() > 16) {
                return AdminConstant.COUNTER_NUMBER_INVALIDATE;
            }
            if (name.length() < 1 || name.length() > 16) {
                return AdminConstant.COUNTER_NAME_INVALIDATE;
            }
        }
        if (!mac.matches(RegExpConstant.MAC_ADDRESS)) {
            return AdminConstant.COUNTER_MAC_INVALIDATE;
        }
        if (!ip.matches(RegExpConstant.IP_ADDRESS)) {
            return AdminConstant.COUNTER_IP_INVALIDATE;
        }
        return null;
    }
}
