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
    ALIVE("/api/v1/alive"),
    //窗口API
    COUNTER_QUERY_BIND("/api/v1/counter/query/bind"),
    COUNTER_CREATE("/api/v1/counter/create"),
    COUNTER_MODIFY("/api/v1/counter/modify"),
    //会话API
    SESSION_APPLY("/api/v1/session/online"),
    //资源API
    RESOURCE_LIST("/api/v1/resource/list"),
    //设置API
    SETTING_QUERY("/api/v1/setting/query");

    String api;

    ApiEnum(String api) {
        this.api = api;
    }

    public String getApi() {
        return api;
    }
}
