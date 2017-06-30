/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.param;

import cn.com.lx1992.lib.client.base.param.BaseParam;

/**
 * @author luoxin
 * @version 2017-6-27
 */
public class CounterQueryBindParam extends BaseParam {
    /**
     * MAC地址
     */
    private String mac;
    /**
     * IP地址
     */
    private String ip;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
