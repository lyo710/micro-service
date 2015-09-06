package com.baidu.ub.msoa.container.support.router.websocket;

import com.baidu.ub.msoa.container.support.router.RouterConstants;
import com.baidu.ub.msoa.container.support.router.event.EventType;
import com.baidu.ub.msoa.container.support.router.event.RouteEvent;
import com.baidu.ub.msoa.event.Event;
import com.baidu.ub.msoa.event.EventHandlerAdapter;
import com.baidu.ub.msoa.event.EventHandlerContext;
import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.ClientEndpoint;
import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by pippo on 15/7/20.
 */
@Component
public class WebSocketRouter extends EventHandlerAdapter<RouteEvent> implements RouterConstants {

    private static Logger logger = LoggerFactory.getLogger(WebSocketRouter.class);
    private static Map<String, ClientEndpoint> endpoints = new ConcurrentHashMap<>();
    private static String WS_URL_FORMAT = "ws://%s:%s" + RPC_ENDPOINT;
    private static KeyedObjectPool<String, Session> pool = new GenericKeyedObjectPool<>(new SessionFactory());

    @Override
    public boolean accept(Event<?> event) {
        return event.getType() == EventType.WEBSOCKET_ROUTE.ordinal();
    }

    @Override
    public void downstream(EventHandlerContext context, RouteEvent event) throws Throwable {
        String url = String.format(WS_URL_FORMAT, event.endpoint.getHost(), event.endpoint.getPort());
        Session session = pool.borrowObject(url);

        try {
            Packet packet = new Packet((byte[]) context.getAttribute(RPC_META),
                    (byte[]) context.getAttribute(RPC_BODY));

            session.getAsyncRemote().sendBinary(packet.to()).get();
            packet = RouteMonitor.createRouteFuture(packet.routeId).get(ROUTE_TIMEOUT, TimeUnit.MILLISECONDS);
            context.setAttribute(RPC_BODY, packet.body);
        } finally {
            pool.returnObject(url, session);
        }

    }

}
