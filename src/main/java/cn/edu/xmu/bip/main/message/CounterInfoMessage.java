/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.main.message;

import cn.edu.xmu.bip.main.meta.MessageTypeEnum;

/**
 * 更新窗口信息Message
 *
 * @author luoxin
 * @version 2017-4-29
 */
public class CounterInfoMessage extends BaseMessage {
    /**
     * 窗口编号
     */
    private String number;
    /**
     * 窗口名称
     */
    private String name;
    /**
     * 资源
     */
    private BaseMessageResource resource;

    public CounterInfoMessage() {
        super(MessageTypeEnum.COUNTER_INFO);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaseMessageResource getResource() {
        return resource;
    }

    public void setResource(BaseMessageResource resource) {
        this.resource = resource;
    }
}
