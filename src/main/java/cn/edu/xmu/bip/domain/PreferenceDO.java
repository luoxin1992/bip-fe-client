/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.domain;

import cn.com.lx1992.lib.client.base.domain.BaseDO;

/**
 * 偏好Domain
 *
 * @author luoxin
 * @version 2017-6-12
 */
public class PreferenceDO extends BaseDO {
    /**
     * 键
     */
    private String key;
    /**
     * 值
     */
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
