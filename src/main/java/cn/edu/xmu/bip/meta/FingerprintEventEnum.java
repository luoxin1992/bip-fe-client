/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.meta;

/**
 * 指纹仪事件枚举
 *
 * @author luoxin
 * @version 2017-6-29
 */
public enum FingerprintEventEnum {
    //指纹仪连接/断开
    SCANNER_CONNECT("scanner-connect"),
    SCANNER_DISCONNECT("scanner-disconnect"),
    //指纹图像捕获/特征提取
    IMAGE_CAPTURE("image-capture"),
    ENROLL_TEMPLATE_EXTRACT("enroll-template-extract"),
    IDENTIFY_TEMPLATE_EXTRACT("identify-template-extract");

    private String event;

    FingerprintEventEnum(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }
}
