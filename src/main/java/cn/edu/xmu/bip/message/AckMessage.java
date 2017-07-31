/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.message;

import cn.edu.xmu.bip.meta.MessageTypeEnum;

/**
 * 确认Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class AckMessage extends BaseSendMessage {
    public AckMessage() {
        super(MessageTypeEnum.ACK);
    }
}
