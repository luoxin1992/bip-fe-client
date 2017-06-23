/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.message;

import cn.edu.xmu.bip.meta.MessageTypeEnum;

/**
 * 指纹登记超时Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class FingerprintEnrollTimeoutMessage extends BaseMessage {
    /**
     * 资源
     */
    private BaseMessageResource resource;

    public FingerprintEnrollTimeoutMessage() {
        super(MessageTypeEnum.FINGERPRINT_ENROLL_TIMEOUT);
    }

    public BaseMessageResource getResource() {
        return resource;
    }

    public void setResource(BaseMessageResource resource) {
        this.resource = resource;
    }
}
