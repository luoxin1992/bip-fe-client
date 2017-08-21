/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.bip.service.impl;

import cn.com.lx1992.lib.client.util.PreferencesUtil;
import cn.edu.xmu.bip.constant.PreferenceKeyConstant;
import cn.edu.xmu.bip.exception.ClientException;
import cn.edu.xmu.bip.exception.ClientExceptionEnum;
import cn.edu.xmu.bip.service.IMessageService;
import cn.edu.xmu.bip.service.IWSService;
import cn.edu.xmu.bip.service.factory.ServiceFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
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
 * WebSocket服务
 *
 * @author luoxin
 * @version 2017-6-26
 */
@ClientEndpoint
public class WSServiceImpl implements IWSService {
    private static final int RECONNECT_MAX = 3;
    private static final String URL_PREFIX;
    private static final boolean URL_VALID;

    private final Logger logger = LoggerFactory.getLogger(WSServiceImpl.class);

    @Inject
    private ServiceFactory serviceFactory;

    //当前会话及重新连接次数
    private Session session;
    private int reconnect = 0;

    static {
        URL_PREFIX = PreferencesUtil.get(PreferenceKeyConstant.CONFIG_BACKEND_WS);
        URL_VALID = !StringUtils.isEmpty(URL_PREFIX);
    }

    @OnOpen
    public void onOpen(Session session) {
        logger.info("connect to web socket server {}", session.getRequestURI());
        //设置session永不超时
        session.setMaxIdleTimeout(0);
        this.session = session;
    }

    @OnMessage(maxMessageSize = 3072)
    public void onMessage(String message) {
        logger.info("receive a {} byte(s) message", message.length());

        IMessageService messageService = (IMessageService) serviceFactory.getInstance(ServiceFactory.MESSAGE);
        messageService.receive(message);
    }

    @OnError
    public void onError(Throwable throwable) {
        logger.error("an error occurred on web socket", throwable);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        logger.info("disconnect from web socket server {} due to {}", session.getRequestURI(), reason.getCloseCode());
        this.session = null;
    }

    @Override
    public void openSession(String token) {
        checkBaseUrl();
        checkSession(false);

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            container.connectToServer(this, URI.create(URL_PREFIX + "?" + token));
            logger.info("open web socket session with token {}", token);
        } catch (DeploymentException | IOException e) {
            logger.error("open web socket session failed", e);
            throw new ClientException(ClientExceptionEnum.WEB_SOCKET_OPEN_SESSION_ERROR);
        }
    }

    @Override
    public void sendMessage(String message) {
        checkSession(true);

        try {
            session.getBasicRemote().sendText(message);
            logger.info("send a {} byte(s) message", message.length());
        } catch (IOException e) {
            logger.error("send message failed", e);
            throw new ClientException(ClientExceptionEnum.WEB_SOCKET_SEND_MESSAGE_ERROR);
        }
    }

    @Override
    public void closeSession(CloseReason.CloseCodes reason) {
        checkSession(true);

        try {
            session.close(new CloseReason(reason, null));
            logger.info("close web socket session for reason {}", reason.getCode());
        } catch (IOException e) {
            logger.error("close web socket session failed", e);
            throw new ClientException(ClientExceptionEnum.WEB_SOCKET_CLOSE_SESSION_ERROR);
        }
    }

    /**
     * 检查当前配置的BASE_URL是否为空
     */
    private void checkBaseUrl() {
        if (!URL_VALID) {
            logger.error("invalid web socket url prefix");
            throw new ClientException(ClientExceptionEnum.WS_URL_PREFIX_INVALID);
        }
    }

    /**
     * 检查WebSocket连接
     *
     * @param exist 连接存在(且未关闭)
     */
    private void checkSession(boolean exist) {
        if (exist) {
            //希望Session存在
            if (session == null) {
                logger.error("web socket session not exist");
                throw new ClientException(ClientExceptionEnum.WEB_SOCKET_SESSION_NOT_EXIST);
            }
            if (!session.isOpen()) {
                logger.error("web socket session is closed");
                throw new ClientException(ClientExceptionEnum.WEB_SOCKET_SESSION_IS_CLOSED);
            }
        } else {
            //希望Session不存在
            if (session != null) {
                logger.error("web socket session already exist");
                throw new ClientException(ClientExceptionEnum.WEB_SOCKET_SESSION_ALREADY_EXIST);
            }
        }
    }
}
