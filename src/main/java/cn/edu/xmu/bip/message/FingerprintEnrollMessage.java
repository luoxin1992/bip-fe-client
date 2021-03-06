/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.message;

import java.util.List;

/**
 * 指纹登记Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class FingerprintEnrollMessage extends BaseReceiveMessage {
    /**
     * 用户(ID)
     */
    private Long user;
    /**
     * 采集次数
     */
    private Integer times;
    /**
     * 超时时间
     */
    private Integer timeout;
    /**
     * 附加信息
     */
    private List<String> extras;

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public List<String> getExtras() {
        return extras;
    }

    public void setExtras(List<String> extras) {
        this.extras = extras;
    }
}
