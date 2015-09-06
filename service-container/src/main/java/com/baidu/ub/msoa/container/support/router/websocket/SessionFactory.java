package com.baidu.ub.msoa.container.support.router.websocket;

import com.baidu.ub.msoa.container.support.router.RouterConstants;
import com.baidu.ub.msoa.container.support.router.websocket.RouteMonitor.RouteFuture;
import com.baidu.ub.msoa.utils.websocket.WebSocketClient;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.ClientEndpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.net.URI;
import java.nio.ByteBuffer;

/**
 * Created by pippo on 15/7/21.
 */
public class SessionFactory extends BaseKeyedPooledObjectFactory<String, Session> implements RouterConstants {

    private static Logger logger = LoggerFactory.getLogger(SessionFactory.class);

    @Override
    public Session create(String uri) throws Exception {
        return WebSocketClient.connect(Endpoint.class, URI.create(uri));

    }

    @Override
    public PooledObject<Session> wrap(Session session) {
        session.setMaxIdleTimeout(1000 * 60 * 10);
        return new DefaultPooledObject<>(session);
    }

    @Override
    public boolean validateObject(String uri, PooledObject<Session> pooledSession) {
        return pooledSession.getObject().isOpen();
    }

    @ClientEndpoint()
    public static class Endpoint {

        @OnOpen
        public void onOpen(Session session, EndpointConfig endpointConfig) {
            logger.debug("create new session:[{}]", session);
        }

        @OnMessage
        public void onMessage(ByteBuffer message, Session session) {
            Packet packet = new Packet().from(message);

            RouteFuture future = null;
            while (future == null) {
                future = RouteMonitor.getRouteFuture(packet.routeId);
            }

            future.packet = packet;
            future.cancel(true);
        }
    }

}
