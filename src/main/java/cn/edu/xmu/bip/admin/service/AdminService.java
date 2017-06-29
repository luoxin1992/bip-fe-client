/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.admin.service;

import cn.com.lx1992.lib.client.util.JsonUtil;
import cn.com.lx1992.lib.client.util.PreferencesUtil;
import cn.edu.xmu.bip.admin.constant.AdminConstant;
import cn.edu.xmu.bip.admin.constant.ApiConstant;
import cn.edu.xmu.bip.common.constant.CommonConstant;
import cn.edu.xmu.bip.admin.dto.BackendEnvDTO;
import cn.edu.xmu.bip.admin.param.CounterCreateParam;
import cn.edu.xmu.bip.admin.param.CounterModifyParam;
import cn.edu.xmu.bip.admin.param.CounterQuerySimpleParam;
import cn.edu.xmu.bip.admin.result.CounterQuerySimpleResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

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

    /**
     * 获取后端环境配置
     *
     * @param env 环境名称
     * @return 后端环境配置
     */
    public BackendEnvDTO getBackend(String env) {
        //若未指定要查询的env，则从偏好中取当前env，若当前env也不存在，直接返回自定义env
        if (StringUtils.isEmpty(env)) {
            env = PreferencesUtil.get(AdminConstant.PREF_BACKEND_ENV);
            if (StringUtils.isEmpty(env)) {
                env = AdminConstant.BACKEND_ENV_CUSTOM;
                logger.warn("backend env not configure yet, fallback to {}", env);
            }
        }
        //3个固有的env从文件中获取，自定义env从偏好中获取
        String api = null, ws = null;
        switch (env) {
            case AdminConstant.BACKEND_ENV_RD:
            case AdminConstant.BACKEND_ENV_QA:
            case AdminConstant.BACKEND_ENV_PROD:
                try (InputStream is = getClass().getResourceAsStream(AdminConstant.INHERENT_BACKEND_FILE)) {
                    JsonNode inherentBackendEnv = JsonUtil.toObject(is);
                    api = inherentBackendEnv.get(env).get(AdminConstant.INHERENT_BACKEND_KEY_API).asText();
                    ws = inherentBackendEnv.get(env).get(AdminConstant.INHERENT_BACKEND_KEY_WS).asText();
                } catch (IOException e) {
                    logger.error("get backend env failed", e);
                }
                break;
            default:
                api = PreferencesUtil.get(AdminConstant.PREF_BACKEND_API);
                ws = PreferencesUtil.get(AdminConstant.PREF_BACKEND_WS);
                break;
        }

        logger.info("get backend env {} successful. api: {}, ws: {}", env, api, ws);
        BackendEnvDTO model = new BackendEnvDTO();
        model.setEnv(env);
        model.setApi(StringUtils.isEmpty(api) ? CommonConstant.EMPTY_STRING : api);
        model.setWs(StringUtils.isEmpty(ws) ? CommonConstant.EMPTY_STRING : ws);
        return model;
    }

    /**
     * 保存后端环境配置
     *
     * @param model 后端环境配置
     */
    public void saveBackend(BackendEnvDTO model) {
        //3个固有的env只需在偏好中保存其名称，自定义env需同时保存url
        switch (model.getEnv()) {
            case AdminConstant.BACKEND_ENV_RD:
            case AdminConstant.BACKEND_ENV_QA:
            case AdminConstant.BACKEND_ENV_PROD:
                PreferencesUtil.put(AdminConstant.PREF_BACKEND_ENV, model.getEnv());
                PreferencesUtil.remove(AdminConstant.PREF_BACKEND_API);
                PreferencesUtil.remove(AdminConstant.PREF_BACKEND_WS);
                break;
            default:
                PreferencesUtil.put(AdminConstant.PREF_BACKEND_ENV, model.getEnv());
                PreferencesUtil.put(AdminConstant.PREF_BACKEND_API, model.getApi());
                PreferencesUtil.put(AdminConstant.PREF_BACKEND_WS, model.getWs());
                break;
        }
        logger.info("save backend env {} successful. api: {}, ws: {}", model.getEnv(), model.getApi(), model.getWs());
    }

    /**
     * 校验后端配置
     *
     * @param api API
     * @param ws  WebSocket
     * @return true-通过，false-不通过
     */
    private boolean validateBackend(String api, String ws) {
        return api.matches(AdminConstant.CUSTOM_BACKEND_API_REGEXP)
                && ws.matches(AdminConstant.CUSTOM_BACKEND_WS_REGEXP);
    }

    public void queryCounter(String mac, String ip,
                             Consumer<CounterQuerySimpleResult> onSuccess, Consumer<String> onFailure) {
        CounterQuerySimpleParam param = new CounterQuerySimpleParam();
        param.setMac(mac);
        param.setIp(ip);
        apiService.invokeCounterQuerySimple(param,
                onSuccess, status -> onFailure.accept(status.getValue() + "(" + status.getKey() + ")"), onFailure);
    }

    public void submitCounter(String number, String name, String mac, String ip,
                              Consumer<Void> onSuccess, Consumer<String> onFailure) {
        CounterQuerySimpleParam bindParam = new CounterQuerySimpleParam();
        bindParam.setMac(mac);
        bindParam.setIp(ip);
        apiService.invokeCounterQuerySimple(bindParam,
                result -> {
                    //调用查询绑定API有返回结果：此窗口已绑定过，本次为修改
                    CounterModifyParam modifyParam = new CounterModifyParam();
                    modifyParam.setId(result.getId());
                    modifyParam.setNumber(number);
                    modifyParam.setName(name);
                    modifyParam.setMac(mac);
                    modifyParam.setIp(ip);
                    apiService.invokeCounterModify(modifyParam, onSuccess, onFailure);
                }, status -> {
                    //调用查询绑定API返回‘未绑定’错误：此窗口未曾绑定过，本次应创建
                    if (status.getKey() == ApiConstant.COUNTER_UNBIND_ERROR) {
                        CounterCreateParam createParam = new CounterCreateParam();
                        createParam.setNumber(number);
                        createParam.setName(name);
                        createParam.setMac(mac);
                        createParam.setIp(ip);
                        apiService.invokeCounterCreate(createParam, onSuccess, onFailure);
                    } else {
                        onFailure.accept(status.getValue() + "(" + status.getKey() + ")");
                    }
                }, onFailure);
    }
}
