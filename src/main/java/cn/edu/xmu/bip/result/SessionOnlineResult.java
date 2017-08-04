/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.result;

import cn.com.lx1992.lib.client.base.result.BaseResult;

/**
 * @author luoxin
 * @version 2017-7-1
 */
public class SessionOnlineResult extends BaseResult {
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
