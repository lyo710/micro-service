package com.baidu.ub.msoa.container.support.router.websocket;

import com.baidu.ub.msoa.container.support.router.RouterConstants;
import com.baidu.ub.msoa.container.support.router.event.EventType;
import com.baidu.ub.msoa.container.support.router.event.RouteEvent;
import com.baidu.ub.msoa.container.support.router.event.RouteMessage;
import com.baidu.ub.msoa.event.Event;
import com.baidu.ub.msoa.event.EventHandler;
import com.baidu.ub.msoa.event.EventHandlerContext;
import com.baidu.ub.msoa.utils.proto.ProtobufCodec;
import org.springframework.stereotype.Component;

/**
 * Created by pippo on 15/7/19.
 */
@Component
public class WebSocketRouteCodec extends ProtobufCodec implements EventHandler<RouteEvent>, RouterConstants {

    @Override
    public String getName() {
        return WebSocketRouteCodec.class.getSimpleName();
    }

    @Override
    public boolean accept(Event<?> event) {
        return event.getType() == EventType.WEBSOCKET_ROUTE.ordinal();
    }

    @Override
    public void downstream(EventHandlerContext context, RouteEvent event) throws Throwable {
        RouteMessage message = event.message;

        byte[] meta = encode(message.meta);
        context.setAttribute(RPC_META, meta);

        byte[] body = encode(message.arguments);
        context.setAttribute(RPC_BODY, body);

        context.passThrough(event);
    }

    @Override
    public void upstream(EventHandlerContext context, RouteEvent event) throws Throwable {
        RouteMessage message = event.message;

        byte[] body = context.getAttribute(RPC_BODY);
        Object[] results = decode(body, message.resultType);
        message.result = results.length > 0 ? results[0] : null;
    }

    @Override
    public void onException(EventHandlerContext context, RouteEvent event, Throwable throwable) {

    }
}
