/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.param;

import cn.com.lx1992.lib.client.base.param.BaseParam;

/**
 * @author luoxin
 * @version 2017-6-28
 */
public class CounterModifyParam extends BaseParam {
    /**
     * ID
     */
    private Long id;
    /**
     * 编号
     */
    private String number;
    /**
     * 名称
     */
    private String name;
    /**
     * MAC地址
     */
    private String mac;
    /**
     * IP地址
     */
    private String ip;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
