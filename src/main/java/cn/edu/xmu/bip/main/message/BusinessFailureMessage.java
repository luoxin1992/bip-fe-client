/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.main.message;

import cn.edu.xmu.bip.main.meta.MessageTypeEnum;

/**
 * 业务受理失败Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class BusinessFailureMessage extends BaseMessage {
    /**
     * 资源
     */
    private BaseMessageResource resource;

    public BusinessFailureMessage() {
        super(MessageTypeEnum.BUSINESS_FAILURE);
    }

    public BaseMessageResource getResource() {
        return resource;
    }

    public void setResource(BaseMessageResource resource) {
        this.resource = resource;
    }
}
