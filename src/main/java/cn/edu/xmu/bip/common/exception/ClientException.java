/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.common.exception;

import cn.com.lx1992.lib.client.base.exception.BaseException;
import cn.com.lx1992.lib.client.base.meta.IResultEnum;

/**
 * 客户端异常
 *
 * @author luoxin
 * @version 2017-6-20
 */
public class ClientException extends BaseException {
    public ClientException(IResultEnum result) {
        super(result);
    }

    public ClientException(IResultEnum result, Object... args) {
        super(result, args);
    }
}
