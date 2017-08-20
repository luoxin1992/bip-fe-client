/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.service.impl;

import cn.com.lx1992.lib.client.base.constant.BaseResponseFieldNameConstant;
import cn.com.lx1992.lib.client.constant.CommonConstant;
import cn.com.lx1992.lib.client.util.HttpUtil;
import cn.com.lx1992.lib.client.util.JsonUtil;
import cn.com.lx1992.lib.client.util.PreferencesUtil;
import cn.edu.xmu.bip.constant.PreferenceKeyConstant;
import cn.edu.xmu.bip.exception.ClientException;
import cn.edu.xmu.bip.exception.ClientExceptionEnum;
import cn.edu.xmu.bip.exception.ServerException;
import cn.edu.xmu.bip.meta.ApiEnum;
import cn.edu.xmu.bip.param.CounterCreateParam;
import cn.edu.xmu.bip.param.CounterModifyParam;
import cn.edu.xmu.bip.param.CounterQueryBindParam;
import cn.edu.xmu.bip.param.SessionApplyParam;
import cn.edu.xmu.bip.param.SettingListParam;
import cn.edu.xmu.bip.result.CounterQueryBindResult;
import cn.edu.xmu.bip.result.ResourceListResult;
import cn.edu.xmu.bip.result.SessionApplyResult;
import cn.edu.xmu.bip.result.SettingQueryResult;
import cn.edu.xmu.bip.service.IAPIService;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class APIServiceImpl implements IAPIService {
    private static final int RESPONSE_CODE_SUCCESS = 0;

    private final Logger logger = LoggerFactory.getLogger(APIServiceImpl.class);

    private static final String URL_PREFIX;
    private static final boolean URL_VALID;

    static {
        URL_PREFIX = PreferencesUtil.get(PreferenceKeyConstant.CONFIG_BACKEND_API);
        URL_VALID = !StringUtils.isEmpty(URL_PREFIX);
    }

    @Override
    public String getUrlPrefix() {
        checkBaseUrl();
        return URL_PREFIX;
    }

    /**
     * 检查当前配置的BASE_URL是否为空
     */
    private void checkBaseUrl() {
        if (!URL_VALID) {
            logger.error("invalid api url prefix");
            throw new ClientException(ClientExceptionEnum.API_URL_PREFIX_INVALID);
        }
    }

    @Override
    public void invokeAlive() {
        invokeBackendApi(ApiEnum.ALIVE, Void.class);
    }

    @Override
    public CounterQueryBindResult invokeCounterQueryBind(CounterQueryBindParam param) {
        return invokeBackendApi(ApiEnum.COUNTER_QUERY_BIND, JsonUtil.toJson(param), CounterQueryBindResult.class);
    }

    @Override
    public void invokeCounterCreate(CounterCreateParam param) {
        invokeBackendApi(ApiEnum.COUNTER_CREATE, JsonUtil.toJson(param), Void.class);
    }

    @Override
    public void invokeCounterModify(CounterModifyParam param) {
        invokeBackendApi(ApiEnum.COUNTER_MODIFY, JsonUtil.toJson(param), Void.class);
    }

    @Override
    public SessionApplyResult invokeSessionApply(SessionApplyParam param) {
        return invokeBackendApi(ApiEnum.SESSION_APPLY, JsonUtil.toJson(param), SessionApplyResult.class);
    }

    @Override
    public ResourceListResult invokeResourceList() {
        return invokeBackendApi(ApiEnum.RESOURCE_LIST, ResourceListResult.class);
    }

    @Override
    public SettingQueryResult invokeSettingList(SettingListParam param) {
        return invokeBackendApi(ApiEnum.SETTING_QUERY, JsonUtil.toJson(param), SettingQueryResult.class);
    }

    /**
     * 调用后端API
     *
     * @param api        API
     * @param resultType 结果类型
     * @return 响应
     */
    private <T> T invokeBackendApi(ApiEnum api, Class<T> resultType) {
        return invokeBackendApi(api, CommonConstant.EMPTY_STRING, resultType);
    }

    /**
     * 调用后端API
     *
     * @param api        API
     * @param request    请求体
     * @param resultType 结果类型
     * @return 响应
     */
    private <T> T invokeBackendApi(ApiEnum api, String request, Class<T> resultType) {
        String url = getUrlPrefix() + api.getApi();
        String response = HttpUtil.executePost(url, request);
        try {
            return convertResponse(resultType, response);
        } catch (ServerException e) {
            logger.warn("invoke backend api {} get error code {}", api, e.getCode());
            throw e;
        }
    }

    /**
     * 转换API响应
     *
     * @param resultType 结果类型
     * @param response   响应体
     * @return 结果
     */
    private <T> T convertResponse(Class<T> resultType, String response) {
        JsonNode root = JsonUtil.toObject(response);

        int code = root.get(BaseResponseFieldNameConstant.CODE).asInt();
        String message = root.get(BaseResponseFieldNameConstant.MESSAGE).asText();

        if (code != RESPONSE_CODE_SUCCESS) {
            throw new ServerException(code, message);
        }

        JsonNode result = root.get(BaseResponseFieldNameConstant.RESULT);
        return result != null ? JsonUtil.toObject(result.toString(), resultType) : null;
    }
}
