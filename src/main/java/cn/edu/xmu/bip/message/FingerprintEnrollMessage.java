/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.message;

import cn.edu.xmu.bip.meta.MessageTypeEnum;

import java.util.List;

/**
 * 指纹登记Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class FingerprintEnrollMessage extends BaseMessage {
    /**
     * 采集次数
     */
    private Integer times;
    /**
     * 资源(长度应等于采集次数)
     */
    private List<BaseMessageResource> resources;

    public FingerprintEnrollMessage() {
        super(MessageTypeEnum.FINGERPRINT_ENROLL);
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public List<BaseMessageResource> getResources() {
        return resources;
    }

    public void setResources(List<BaseMessageResource> resources) {
        this.resources = resources;
    }
}