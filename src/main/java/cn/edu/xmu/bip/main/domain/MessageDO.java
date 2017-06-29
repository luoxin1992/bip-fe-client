/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.main.domain;

import cn.com.lx1992.lib.client.base.domain.BaseDO;

/**
 * 消息日志Domain
 *
 * @author luoxin
 * @version 2017-6-12
 */
public class MessageDO extends BaseDO {
    /**
     * 类型
     */
    private String type;
    /**
     * 消息体
     */
    private String body;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
