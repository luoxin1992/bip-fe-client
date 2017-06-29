/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.main.message;

import cn.edu.xmu.bip.main.meta.MessageTypeEnum;

/**
 * 业务暂停受理Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class BusinessPauseMessage extends BaseMessage {
    /**
     * 资源
     */
    private BaseMessageResource resource;

    public BusinessPauseMessage() {
        super(MessageTypeEnum.BUSINESS_PAUSE);
    }

    public BaseMessageResource getResource() {
        return resource;
    }

    public void setResource(BaseMessageResource resource) {
        this.resource = resource;
    }
}
