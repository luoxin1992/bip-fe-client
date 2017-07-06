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
    //参数配置
    //后端环境
    //预置后端环境
    public static final String CONFIG_BACKEND_PRESET_FILE = "/file/preset-backend.json";
    public static final String CONFIG_BACKEND_PRESET_KEY_API = "api";
    public static final String CONFIG_BACKEND_PRESET_KEY_WS = "ws";
    //校验正则
    public static final String CONFIG_BACKEND_REG_EXP_API = "(http|https)://.*";
    public static final String CONFIG_BACKEND_REG_EXP_WS = "(ws|wss)://.*";
    //提示信息
    public static final String CONFIG_BACKEND_NOT_CONFIGURE = "后端环境尚未配置.";
    public static final String CONFIG_BACKEND_PRESET_UNAVAILABLE = "预置后端环境{0}暂不可用.";
    public static final String CONFIG_BACKEND_API_INVALIDATE = "API格式错误.";
    public static final String CONFIG_BACKEND_WS_INVALIDATE = "WebSocket格式错误.";
    public static final String CONFIG_BACKEND_SAVE_SUCCESSFUL = "保存成功，重新启动程序后生效.";
    //窗口信息
    //提示信息
    public static final String CONFIG_COUNTER_NUMBER_INVALIDATE = "窗口编号长度应在[1, 16]之间.";
    public static final String CONFIG_COUNTER_NAME_INVALIDATE = "窗口名称长度应在[1, 32]之间.";
    public static final String CONFIG_COUNTER_MAC_INVALIDATE = "MAC地址格式错误.";
    public static final String CONFIG_COUNTER_IP_INVALIDATE = "IP地址格式错误.";
    public static final String CONFIG_COUNTER_SUBMIT_SUCCESSFUL = "提交成功.";
    //数据浏览
    //数据库表
    public static final String TABLE_FINGERPRINT_LOG = "fingerprint-log";
    public static final String TABLE_MESSAGE_LOG = "message-log";
    public static final String TABLE_RESOURCE = "resource";
    //单次查询上限
    public static final int MAX_QUERY_COUNT = 1000;
    //提示信息
    public static final String DATA_BROWSING_TABLE_NOT_SELECT = "请选择要查询的数据库表.";
    public static final String DATA_BROWSING_DATE_NOT_SET = "开始日期和结束日期不能为空.";
    public static final String DATA_BROWSING_DATE_INVALID = "开始日期不能晚于结束日期.";
    public static final String DATA_BROWSING_QUERY_SUCCESSFUL = "查询结果共{0}行.";
}
