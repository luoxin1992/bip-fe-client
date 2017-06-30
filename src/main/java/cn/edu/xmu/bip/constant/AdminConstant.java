/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.constant;

/**
 * 管理工具常量
 *
 * @author luoxin
 * @version 2017-6-27
 */
public class AdminConstant {
    //预置后端环境
    public static final String PRESET_BACKEND_FILE = "/config/preset-backend.json";
    public static final String PRESET_BACKEND_API_KEY = "api";
    public static final String PRESET_BACKEND_WS_KEY = "ws";
    //后端环境名称
    public static final String BACKEND_ENV_RD = "rd";
    public static final String BACKEND_ENV_QA = "qa";
    public static final String BACKEND_ENV_PROD = "prod";
    public static final String BACKEND_ENV_CUSTOM = "custom";
    //后端环境偏好Key值
    public static final String BACKEND_ENV_PREF = "backend-env";
    public static final String BACKEND_CUSTOM_API_PREF = "backend-custom-api";
    public static final String BACKEND_CUSTOM_WS_PREF = "backend-custom-ws";
    public static final String BACKEND_CURRENT_API_PREF = "backend-current-api";
    public static final String BACKEND_CURRENT_WS_PREF = "backend-current-ws";
    //后端环境校验正则
    public static final String BACKEND_API_REG_EXP = "(http|https)://.*";
    public static final String BACKEND_WS_REG_EXP = "(ws|wss)://.*";
    //保存后端配置提示
    public static final String BACKEND_SAVE_SUCCESSFUL = "保存成功，重新启动程序后生效.";
    public static final String BACKEND_API_INVALIDATE = "API格式错误.";
    public static final String BACKEND_WS_INVALIDATE = "WebSocket格式错误.";
    //提交窗口信息提示
    public static final String COUNTER_SUBMIT_SUCCESS = "提交成功.";
    public static final String COUNTER_NUMBER_INVALIDATE = "窗口编号长度应在[1, 16]之间.";
    public static final String COUNTER_NAME_INVALIDATE = "窗口名称长度应在[1, 32]之间.";
    public static final String COUNTER_MAC_INVALIDATE = "MAC地址格式错误.";
    public static final String COUNTER_IP_INVALIDATE = "IP地址格式错误.";
    //数据库表
    public static final String TABLE_FINGERPRINT_LOG = "fingerprint-log";
    public static final String TABLE_MESSAGE_LOG = "message-log";
    public static final String TABLE_RESOURCE = "resource";
}
