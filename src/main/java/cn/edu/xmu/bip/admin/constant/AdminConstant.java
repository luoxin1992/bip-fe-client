/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.admin.constant;

/**
 * 管理工具常量
 *
 * @author luoxin
 * @version 2017-6-27
 */
public class AdminConstant {
    //固有后端环境描述
    public static final String INHERENT_BACKEND_FILE = "/config/inherent-backend.json";
    public static final String INHERENT_BACKEND_KEY_API = "api";
    public static final String INHERENT_BACKEND_KEY_WS = "ws";
    //后端环境名称
    public static final String BACKEND_ENV_RD = "rd";
    public static final String BACKEND_ENV_QA = "qa";
    public static final String BACKEND_ENV_PROD = "prod";
    public static final String BACKEND_ENV_CUSTOM = "custom";
    //后端环境偏好Key值
    public static final String PREF_BACKEND_ENV = "backend-env";
    public static final String PREF_BACKEND_API = "backend-api";
    public static final String PREF_BACKEND_WS = "backend-ws";
    //自定义后端环境校验正则
    public static final String CUSTOM_BACKEND_API_REGEXP = "(http|https)://.*";
    public static final String CUSTOM_BACKEND_WS_REGEXP = "(ws|wss)://.*";
    //保存后端配置提示
    public static final String BACKEND_SAVE_SUCCESSFUL = "保存成功，重新启动程序后生效.";
    public static final String BACKEND_API_INVALIDATE = "API格式错误.";
    public static final String BACKEND_WS_INVALIDATE = "WebSocket格式错误.";
    //提交窗口信息提示
    public static final String COUNTER_SUBMIT_SUCCESS = "提交成功.";
    public static final String COUNTER_NUMBER_INVALIDATE = "窗口编号长度应在[1, 16]之间.";
    public static final String COUNTER_NAME_INVALIDATE = "窗口名称长度应在[1, 32]之间.";

}
