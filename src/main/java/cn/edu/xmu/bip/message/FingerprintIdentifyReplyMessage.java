/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.message;

import cn.edu.xmu.bip.meta.MessageTypeEnum;

/**
 * 指纹辨识回复Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class FingerprintIdentifyReplyMessage extends BaseSendMessage {
    /**
     * 状态
     */
    private String status;
    /**
     * 指纹模型
     */
    private String template;

    public FingerprintIdentifyReplyMessage() {
        super(MessageTypeEnum.FINGERPRINT_IDENTIFY_REPLY);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
