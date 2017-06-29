/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.main.message;

import cn.edu.xmu.bip.main.meta.MessageTypeEnum;

/**
 * 业务正在受理Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class BusinessProcessMessage extends BaseMessage {
    /**
     * 资源
     */
    private BaseMessageResource resource;

    public BusinessProcessMessage() {
        super(MessageTypeEnum.BUSINESS_PROCESS);
    }

    public BaseMessageResource getResource() {
        return resource;
    }

    public void setResource(BaseMessageResource resource) {
        this.resource = resource;
    }
}
