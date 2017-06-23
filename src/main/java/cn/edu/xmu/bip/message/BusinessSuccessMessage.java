/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.message;

import cn.edu.xmu.bip.meta.MessageTypeEnum;

/**
 * 业务受理成功Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class BusinessSuccessMessage extends BaseMessage {
    /**
     * 资源
     */
    private BaseMessageResource resource;

    public BusinessSuccessMessage() {
        super(MessageTypeEnum.BUSINESS_SUCCESS);
    }

    public BaseMessageResource getResource() {
        return resource;
    }

    public void setResource(BaseMessageResource resource) {
        this.resource = resource;
    }
}
