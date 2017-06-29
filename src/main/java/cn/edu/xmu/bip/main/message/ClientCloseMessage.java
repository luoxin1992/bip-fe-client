/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.main.message;

import cn.edu.xmu.bip.main.meta.MessageTypeEnum;

/**
 * 关闭客户端Message
 *
 * @author luoxin
 * @version 2017-4-29
 */
public class ClientCloseMessage extends BaseMessage {
    public ClientCloseMessage() {
        super(MessageTypeEnum.CLIENT_CLOSE);
    }
}
