/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.meta;

/**
 * API接口地址枚举
 *
 * @author luoxin
 * @version 2017-7-27
 */
public enum ApiEnum {
    //窗口API
    COUNTER_QUERY_BIND("/api/v1/counter/query/bind"),
    COUNTER_CREATE("/api/v1/counter/create"),
    COUNTER_MODIFY("/api/v1/counter/modify"),
    //会话API
    SESSION_ONLINE("/api/v1/session/online"),
    SESSION_OFFLINE("/api/v1/session/offline"),
    //资源API
    RESOURCE_LIST("/api/v1/resource/list"),
    //设置API
    SETTING_LIST("/api/v1/setting/list");

    String api;

    ApiEnum(String api) {
        this.api = api;
    }

    public String getApi() {
        return api;
    }
}
