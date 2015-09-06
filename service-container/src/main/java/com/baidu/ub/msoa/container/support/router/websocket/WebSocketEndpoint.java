/*
 * Copyright © 2010 www.myctu.cn. All rights reserved.
 */

package com.baidu.ub.msoa.container.support.router.websocket;

import com.baidu.ub.msoa.container.BundleContainer;
import com.baidu.ub.msoa.container.support.router.RouterConstants;
import com.baidu.ub.msoa.container.support.router.event.EventType;
import com.baidu.ub.msoa.container.support.router.event.InputEvent;
import com.baidu.ub.msoa.event.EventCallback;
import com.baidu.ub.msoa.event.EventEngine;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/**
 * User: pippo
 * Date: 14-2-24-12:14
 */
@ServerEndpoint(RouterConstants.RPC_ENDPOINT)
public class WebSocketEndpoint {

    private static Logger logger = LoggerFactory.getLogger(WebSocketEndpoint.class);
    private static Map<String, Executor> executors = new ConcurrentHashMap<>();

    private EventEngine engine;

    public EventEngine getEngine() {
        if (engine == null) {
            engine = BundleContainer.get().getBean("event.process.engine");
        }

        return engine;
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        logger.debug("accept new session:[{}]", session);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        logger.debug("remove session:[{}]", session);
    }

    @OnMessage
    public void onMessage(byte[] input, final Session session) {
        InputEvent event = new InputEvent(EventType.WEBSOCKET_INPUT, input);

        getEngine().process(event, new EventCallback<InputEvent>() {

            @Override
            public void preInvoke(InputEvent event) {
            }

            @Override
            public void onSuccess(InputEvent event) {
                try {
                    session.getAsyncRemote().sendBinary(ByteBuffer.wrap(event.output)).get();
                } catch (Exception e) {
                    logger.error("write back message due to error", e);
                }
            }

            @Override
            public void onFail(InputEvent event, Throwable t) {
                logger.error("process event:[{}] due to error:[{}]", event, ExceptionUtils.getStackTrace(t));
                // TODO 返回错误告知消息
            }
        });
    }

}
