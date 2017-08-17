/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.message;

import cn.edu.xmu.bip.meta.MessageTypeEnum;

/**
 * 接收的消息基类
 *
 * @author luoxin
 * @version 2017-5-19
 */
class BaseSendMessage extends BaseMessage {
    BaseSendMessage(MessageTypeEnum type) {
        super(type);
    }
}
