/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.main.message;

import cn.edu.xmu.bip.main.meta.MessageTypeEnum;

/**
 * 指纹辨识回复Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class FingerprintIdentifyReplyMessage extends BaseMessage {
    /**
     * 指纹模型
     */
    private String template;

    public FingerprintIdentifyReplyMessage() {
        super(MessageTypeEnum.FINGERPRINT_IDENTIFY_REPLY);
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
