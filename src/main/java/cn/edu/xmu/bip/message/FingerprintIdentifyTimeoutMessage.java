/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.message;

import cn.edu.xmu.bip.meta.MessageTypeEnum;

/**
 * 指纹辨识超时Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class FingerprintIdentifyTimeoutMessage extends BaseMessage {
    /**
     * 资源
     */
    private BaseMessageResource resource;

    public FingerprintIdentifyTimeoutMessage() {
        super(MessageTypeEnum.FINGERPRINT_IDENTIFY_TIMEOUT);
    }

    public BaseMessageResource getResource() {
        return resource;
    }

    public void setResource(BaseMessageResource resource) {
        this.resource = resource;
    }
}
