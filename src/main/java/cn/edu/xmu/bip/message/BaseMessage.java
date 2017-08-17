/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.message;

import cn.edu.xmu.bip.meta.MessageTypeEnum;

/**
 * 消息基类
 *
 * @author luoxin
 * @version 2017-5-19
 */
public class BaseMessage {
    /**
     * UID
     */
    private Long uid;
    /**
     * 类型
     */
    private String type;

    BaseMessage() {
    }

    BaseMessage(MessageTypeEnum type) {
        this.type = type.getType();
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
