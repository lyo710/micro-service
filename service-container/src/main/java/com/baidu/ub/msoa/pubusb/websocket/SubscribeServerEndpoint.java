package com.baidu.ub.msoa.pubusb.websocket;

import com.baidu.ub.msoa.pubusb.Constants;
import com.baidu.ub.msoa.pubusb.SubscriberManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 * Created by pippo on 15/8/3.
 */
// @ServerEndpoint("/subscribe")
public abstract class SubscribeServerEndpoint implements Constants {

    private static Logger logger = LoggerFactory.getLogger(SubscribeServerEndpoint.class);

    @OnOpen
    public void onOpen(final Session session, EndpointConfig endpointConfig) {
        logger.debug("accept new session:[{}]", session.getId());
        session.setMaxIdleTimeout(SESSION_TIMEOUT);
        getSubscriberManager().add(new WebSocketSubscriberPartner(session));
    }

    @OnClose
    public void onClose(Session session) {
        String sessionId = session.getId();
        getSubscriberManager().remove(sessionId);
        logger.debug("remove session:[{}]", sessionId);
    }

    //    @OnMessage
    //    public void onMessage(String msg, Session session) {
    //        logger.debug("receive message:[{}]", msg);
    //    }

    protected abstract SubscriberManager getSubscriberManager();

}
