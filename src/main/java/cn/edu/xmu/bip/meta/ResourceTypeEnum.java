/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.meta;

/**
 * 资源类型枚举
 *
 * @author luoxin
 * @version 2017-6-29
 */
public enum ResourceTypeEnum {
    VOICE("voice", "语音"),
    IMAGE("image", "图片"),
    PHOTO("photo", "照片");

    private String type;
    private String description;

    ResourceTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
