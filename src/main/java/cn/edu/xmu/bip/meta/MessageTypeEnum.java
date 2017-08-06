/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.meta;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * 消息类型枚举
 *
 * @author luoxin
 * @version 2017-4-27
 */
public enum MessageTypeEnum {
    UNKNOWN(null, "未知"),
    //接收的消息
    //服务控制
    SERVICE_CANCEL("service-cancel", "取消服务"),
    SERVICE_PAUSE("service-pause", "暂停服务"),
    SERVICE_RESUME("service-resume", "恢复服务"),
    //一般业务
    GENERAL_BUSINESS("general-business", "一般业务"),
    GENERAL_BUSINESS_SUCCESS("general-business-success", "一般业务成功"),
    GENERAL_BUSINESS_FAILURE("general-business-failure", "一般业务失败"),
    //指纹登记
    FINGERPRINT_ENROLL("fingerprint-enroll", "指纹登记"),
    FINGERPRINT_ENROLL_SUCCESS("fingerprint-enroll-success", "指纹登记成功"),
    FINGERPRINT_ENROLL_FAILURE("fingerprint-enroll-failure", "指纹登记失败"),
    //指纹辨识
    FINGERPRINT_IDENTIFY("fingerprint-identify", "指纹辨识"),
    FINGERPRINT_IDENTIFY_SUCCESS("fingerprint-identify-success", "指纹辨识成功"),
    FINGERPRINT_IDENTIFY_FAILURE("fingerprint-identify-failure", "指纹辨识失败"),
    //更新界面
    UPDATE_COMPANY_INFO("update-company-info", "更新公司信息"),
    UPDATE_COUNTER_INFO("update-counter-info", "更新窗口信息"),
    UPDATE_USER_INFO("update-user-info", "更新用户信息"),
    //发送的消息
    ACK("ack", "确认"),
    FINGERPRINT_ENROLL_REPLY("fingerprint-enroll-reply", "指纹登记回复"),
    FINGERPRINT_IDENTIFY_REPLY("fingerprint-identify-reply", "指纹辨识回复");

    private String type;
    private String description;

    MessageTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static MessageTypeEnum getByType(String type) {
        Optional<MessageTypeEnum> result = Arrays.stream(values())
                .filter(value -> Objects.equals(value.type, type))
                .findFirst();
        return result.orElse(UNKNOWN);
    }
}