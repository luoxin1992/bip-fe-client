/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.service;

import javax.websocket.CloseReason;

/**
 * WebSocket Service
 * <p>
 * 建立和关闭连接
 * 发送编码的消息
 *
 * @author luoxin
 * @version 2017-8-3
 */
public interface IWSService extends IBaseService {
    /**
     * 建立WebSocket连接
     *
     * @param token Token
     */
    void openSession(String token);

    /**
     * 发送WebSocket消息
     *
     * @param message 消息
     */
    void sendMessage(String message);

    /**
     * 关闭WebSocket会话
     *
     * @param reason 原因
     */
    void closeSession(CloseReason.CloseCodes reason);
}
