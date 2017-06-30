/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.message;

import cn.edu.xmu.bip.meta.MessageTypeEnum;

/**
 * 指纹登记回复Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class FingerprintEnrollReplyMessage extends BaseMessage {
    /**
     * 指纹模型
     */
    private String template;

    public FingerprintEnrollReplyMessage() {
        super(MessageTypeEnum.FINGERPRINT_ENROLL_REPLY);
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
