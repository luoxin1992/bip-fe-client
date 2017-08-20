/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.exception;

import cn.com.lx1992.lib.client.base.exception.BaseException;

/**
 * 服务端异常
 *
 * @author luoxin
 * @version 2017-6-20
 */
public class ServerException extends BaseException {
    public ServerException(Integer code, String message) {
        super(code, message);
    }
}
