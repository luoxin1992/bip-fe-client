/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.message;

import cn.edu.xmu.bip.meta.MessageTypeEnum;

/**
 * 指纹辨识Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class FingerprintIdentifyMessage extends BaseMessage {
    /**
     * 超时时间
     */
    private Integer timeout;
    /**
     * 资源
     */
    private BaseMessageResource resource;

    public FingerprintIdentifyMessage() {
        super(MessageTypeEnum.FINGERPRINT_IDENTIFY);
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public BaseMessageResource getResource() {
        return resource;
    }

    public void setResource(BaseMessageResource resource) {
        this.resource = resource;
    }
}
