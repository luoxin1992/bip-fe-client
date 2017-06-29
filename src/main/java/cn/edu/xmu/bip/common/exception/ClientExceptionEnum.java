/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.common.exception;

import cn.com.lx1992.lib.client.base.meta.IResultEnum;

/**
 * @author luoxin
 * @version 2017-6-20
 */
public enum ClientExceptionEnum implements IResultEnum {
    DATABASE_INSERT_ERROR(20101, "数据库新增记录出错"),
    DATABASE_UPDATE_ERROR(20102, "数据库新增记录出错"),
    DATABASE_DELETE_ERROR(20103, "数据库删除记录出错"),
    DATABASE_SELECT_ERROR(20104, "数据库查询记录出错"),;

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
