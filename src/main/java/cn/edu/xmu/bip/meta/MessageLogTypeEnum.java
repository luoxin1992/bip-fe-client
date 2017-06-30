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
public enum MessageLogTypeEnum {
    UNKNOWN("unknown", "未知");

    private String type;
    private String description;

    MessageLogTypeEnum(String type, String description) {
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
