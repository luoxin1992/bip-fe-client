/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.domain;

import cn.com.lx1992.lib.client.base.domain.BaseDO;

/**
 * 指纹Domain
 *
 * @author luoxin
 * @version 2017-6-12
 */
public class FingerprintDO extends BaseDO {
    /**
     * 事件
     */
    private String event;
    /**
     * 附加信息
     */
    private String extra;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
