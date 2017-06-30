/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;

/**
 * WebSocket通信服务
 *
 * @author luoxin
 * @version 2017-6-26
 */
@ClientEndpoint
public class WebSocketService {
    private final Logger logger = LoggerFactory.getLogger(WebSocketService.class);

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        logger.info("connect to ws server successful, session id {}", session.getId());
    }

    @OnMessage
    public void onMessage(String message) {

    }

    @OnError
    public void onError(Throwable throwable) {

    }

    @OnClose
    public void onClose(CloseReason reason) {

    }

    public void connectServer(String url, String token) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            logger.info("start connecting ws server {}", url);
            container.connectToServer(this, URI.create(url + "?" + token));
        } catch (DeploymentException | IOException e) {
            logger.error("connect to ws server failed", e);
        }
    }

    public void sendMessage(String message) {
        if (session == null || !session.isOpen()) {
            logger.error("session is close or not connect yet");
            //throw new ClientException();
        }
        session.getAsyncRemote().sendText(message, result -> {
            if (result.isOK()) {

            } else {

            }
        });
    }

    public void closeSession(CloseReason reason) {
        try {
            logger.info("try disconnecting from ws server");
            session.close(reason);
            session = null;
        } catch (IOException e) {
            logger.error("disconnect from ws server failed", e);
        }
    }
}
