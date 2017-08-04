/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.param;

import cn.com.lx1992.lib.client.base.param.BaseParam;

/**
 * @author luoxin
 * @version 2017-7-1
 */
public class SessionOfflineParam extends BaseParam {
    /**
     * Token
     */
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
