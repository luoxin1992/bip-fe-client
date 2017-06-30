/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.service;

import cn.com.lx1992.lib.client.base.constant.BaseFieldNameConstant;
import cn.com.lx1992.lib.client.util.HttpUtil;
import cn.com.lx1992.lib.client.util.JsonUtil;
import cn.com.lx1992.lib.client.util.PreferencesUtil;
import cn.edu.xmu.bip.constant.AdminConstant;
import cn.edu.xmu.bip.constant.ApiConstant;
import cn.edu.xmu.bip.param.CounterCreateParam;
import cn.edu.xmu.bip.param.CounterModifyParam;
import cn.edu.xmu.bip.param.CounterQueryBindParam;
import cn.edu.xmu.bip.result.CounterQuerySimpleResult;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 后端API调用
 *
 * @author luoxin
 * @version 2017-6-26
 */
public class ApiService {
    private final Logger logger = LoggerFactory.getLogger(ApiService.class);

    private static final String API_BASE_URL;

    static {
        API_BASE_URL = PreferencesUtil.get(AdminConstant.BACKEND_CURRENT_API_PREF);
    }

    void invokeCounterQueryBind(CounterQueryBindParam param, Consumer<CounterQuerySimpleResult> onSuccess,
                                Consumer<Map.Entry<Integer, String>> onFailure, Consumer<String> onError) {
        invokeBackendApi(API_BASE_URL + ApiConstant.COUNTER_QUERY_BIND,
                JsonUtil.toJson(param), CounterQuerySimpleResult.class, onSuccess, onFailure, onError);
    }

    void invokeCounterCreate(CounterCreateParam param, Consumer<Void> onSuccess, Consumer<String> onFailure) {
        invokeBackendApi(API_BASE_URL + ApiConstant.COUNTER_CREATE,
                JsonUtil.toJson(param), Void.class,
                onSuccess, status -> onFailure.accept(status.getValue()), onFailure);
    }

    void invokeCounterModify(CounterModifyParam param, Consumer<Void> onSuccess, Consumer<String> onFailure) {
        invokeBackendApi(API_BASE_URL + ApiConstant.COUNTER_MODIFY,
                JsonUtil.toJson(param), Void.class,
                onSuccess, status -> onFailure.accept(status.getValue()), onFailure);
    }

    private <T> void invokeBackendApi(String api, String request,
                                      Class<T> resultType, Consumer<T> onSuccess,
                                      Consumer<Map.Entry<Integer, String>> onFailure, Consumer<String> onError) {
        HttpUtil.executePostAsync(api, request,
                response -> {
                    Map.Entry<Integer, String> status = convertJsonResponse(response);
                    if (status.getKey() == ApiConstant.SUCCESS) {
                        T result = convertJsonResponse(resultType, response);
                        onSuccess.accept(result);
                    } else {
                        onFailure.accept(status);
                    }
                },
                errorCode -> onError.accept("调用后端API错误：HTTP " + errorCode),
                errorInfo -> onError.accept("调用后端API错误：" + errorInfo));
    }

    private Map.Entry<Integer, String> convertJsonResponse(String response) {
        JsonNode root = JsonUtil.toObject(response);
        int code = root.get(BaseFieldNameConstant.CODE).asInt();
        String message = root.get(BaseFieldNameConstant.MESSAGE).asText();
        return new AbstractMap.SimpleEntry<>(code, message);
    }

    private <T> T convertJsonResponse(Class<T> clazz, String response) {
        JsonNode root = JsonUtil.toObject(response);
        JsonNode result = root.get(BaseFieldNameConstant.RESULT);
        return result != null ? JsonUtil.toObject(result.toString(), clazz) : null;
    }
}
