/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.message;

/**
 * 指纹登记失败Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class FingerprintEnrollFailureMessage extends BaseReceiveMessage {
    /**
     * 附加信息
     */
    private String extra;

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
