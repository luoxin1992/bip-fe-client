/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.exception;

import cn.com.lx1992.lib.client.base.meta.IResultEnum;

/**
 * 客户端异常枚举
 *
 * @author luoxin
 * @version 2017-6-20
 */
public enum ClientExceptionEnum implements IResultEnum {
    //单例工厂
    INVALID_DAO_NAME(20101, "无效DAO实例名称."),
    INVALID_SERVICE_NAME(20202, "无效Service实例名称."),
    INVALID_STAGE_NAME(20203, "无效Stage实例名称."),
    //数据库
    DB_READ_SQL_ERROR(20201, "SQL文件读取出错."),
    DB_EXECUTE_SQL_ERROR(20202, "SQL语句执行出错."),
    DB_INSERT_ERROR(20203, "数据库插入出错."),
    DB_UPDATE_ERROR(20204, "数据库更新出错."),
    DB_SELECT_ERROR(20205, "数据库查询出错."),
    //WebSocket服务
    WEB_SOCKET_SESSION_NOT_EXIST(20301, "WebSocket会话不存在."),
    WEB_SOCKET_SESSION_IS_CLOSED(20302, "WebSocket会话已关闭."),
    WEB_SOCKET_SESSION_ALREADY_EXIST(20303, "WebSocket会话已存在."),
    WEB_SOCKET_OPEN_SESSION_ERROR(20304, "开启WebSocket会话出错."),
    WEB_SOCKET_SEND_MESSAGE_ERROR(20305, "发送WebSocket消息出错."),
    WEB_SOCKET_CLOSE_SESSION_ERROR(20306, "关闭WebSocket会话出错."),
    //消息服务
    MESSAGE_IDEMPOTENT_BROKEN(20401, "消息不符合幂等原则."),
    MESSAGE_UNKNOWN_TYPE(20401, "无法识别的消息类型."),
    MESSAGE_SERVICE_STATE_MISMATCH(20401, "当前服务状态不满足前置条件."),
    MESSAGE_SERVICE_TYPE_MISMATCH(20402, "当前服务类型不满足前置条件."),
    //杂项服务
    MISC_UNSUPPORTED_OS(20301, "本程序仅支持Windows操作系统，您当前使用的是{0}操作系统."),
    MISC_UNSUPPORTED_JVM(20302, "本程序仅支持32位Java虚拟机，您当前使用的是{0}位Java虚拟机."),
    MISC_BIND_NIC_NOT_EXIST(20303, "未能获取客户端绑定的网卡，请运行管理工具."),
    MISC_COUNTER_ID_NOT_EXIST(20304, "未能获取客户端绑定的窗口ID，请运行管理工具."),
    //API或WS URL配置错误
    API_URL_PREFIX_INVALID(20401, "后端环境配置有误，无法调用API."),
    WS_URL_PREFIX_INVALID(20401, "后端环境配置有误，无法连接WebSocket."),
    //管理工具
    ADMIN_CONFIG_BACKEND_NOT_CONFIGURE(20401, "尚未配置后端环境."),
    ADMIN_CONFIG_BACKEND_PRESET_UNAVAILABLE(20402, "预置后端环境{0}不可用."),
    ADMIN_CONFIG_BACKEND_PRESET_ERROR(20403, "获取预置后端环境出错."),
    ADMIN_CONFIG_BACKEND_INVALID_PARAM_API(20404, "参数错误：API格式不正确."),
    ADMIN_CONFIG_BACKEND_INVALID_PARAM_WS(20405, "参数错误：WebSocket格式不正确."),
    ADMIN_CONFIG_COUNTER_NIC_NOT_BIND(20406, "尚未选择绑定网卡."),
    ADMIN_CONFIG_COUNTER_INVALID_PARAM_NUMBER(20407, "参数错误：窗口编号长度应在[1, 16]之间."),
    ADMIN_CONFIG_COUNTER_INVALID_PARAM_NAME(20408, "参数错误：窗口名称长度应在[1, 32]之间."),
    ADMIN_CONFIG_COUNTER_INVALID_PARAM_MAC(20409, "参数错误：MAC地址格式不正确."),
    ADMIN_CONFIG_COUNTER_INVALID_PARAM_IP(20410, "参数错误：IP地址格式不正确."),
    ADMIN_DATA_BROWSING_DATE_NULL(20411, "开始日期和结束日期不能为空."),
    ADMIN_DATA_BROWSING_DATE_INVALID(20412, "开始日期不能晚于结束日期."),
    ADMIN_DATA_BROWSING_RESULT_SET_TOO_LONG(20413, "查询结果太多({0,number,#}条)."),
    //启动错误
    STARTUP_LOAD_PROP_ERROR(20501, "加载属性文件出错."),
    STARTUP_CLI_ARGS_INVALID(20501, "无效的命令行参数.");

    private Integer code;
    private String message;

    ClientExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
