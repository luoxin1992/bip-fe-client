/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.main.message;

import cn.edu.xmu.bip.main.meta.MessageTypeEnum;

/**
 * 指纹辨识失败Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class FingerprintIdentifyFailureMessage extends BaseMessage {
    /**
     * 资源
     */
    private BaseMessageResource resource;

    public FingerprintIdentifyFailureMessage() {
        super(MessageTypeEnum.FINGERPRINT_IDENTIFY_FAILURE);
    }

    public BaseMessageResource getResource() {
        return resource;
    }

    public void setResource(BaseMessageResource resource) {
        this.resource = resource;
    }
}
