/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.meta;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author luoxin
 * @version 2017-6-29
 */
public enum FingerprintLogTypeEnum {
    DEVICE_CONNECT("device-connect", "连接指纹扫描仪"),
    DEVICE_DISCONNECT("device-disconnect", "断开指纹扫描仪"),
    IMAGE_CAPTURE("image-capture", "捕获指纹图像"),
    FINGERPRINT_ENROLL("fingerprint-enroll", "生成指纹登记模板"),
    FINGERPRINT_IDENTIFY("fingerprint-identify", "生成指纹辨识模板"),
    UNKNOWN("unknown", "未知");

    private String type;
    private String description;

    FingerprintLogTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static String getDescriptionByType(String type) {
        Optional<String> result = Arrays.stream(values())
                .filter(value -> type.equals(value.type))
                .map(value -> value.description)
                .findFirst();
        return result.orElse(UNKNOWN.description);
    }
}
