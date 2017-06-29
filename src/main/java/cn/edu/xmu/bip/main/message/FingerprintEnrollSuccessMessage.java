/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.main.message;

import cn.edu.xmu.bip.main.meta.MessageTypeEnum;

/**
 * 指纹登记成功Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class FingerprintEnrollSuccessMessage extends BaseMessage {
    /**
     * 资源
     */
    private BaseMessageResource resource;

    public FingerprintEnrollSuccessMessage() {
        super(MessageTypeEnum.FINGERPRINT_ENROLL_SUCCESS);
    }

    public BaseMessageResource getResource() {
        return resource;
    }

    public void setResource(BaseMessageResource resource) {
        this.resource = resource;
    }
}
