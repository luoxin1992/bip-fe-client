/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.admin.dto;

/**
 * 管理工具-后端环境VO
 *
 * @author luoxin
 * @version 2017-6-27
 */
public class BackendEnvDTO {
    private String env;
    private String api;
    private String ws;

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getWs() {
        return ws;
    }

    public void setWs(String ws) {
        this.ws = ws;
    }
}
