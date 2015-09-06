package com.baidu.ub.msoa.container.support.router.http;

import com.baidu.ub.msoa.container.support.router.RouterConstants;
import com.baidu.ub.msoa.container.support.router.event.EventType;
import com.baidu.ub.msoa.container.support.router.event.RouteEvent;
import com.baidu.ub.msoa.container.support.router.event.RouteMessage;
import com.baidu.ub.msoa.container.support.router.event.RouteMeta;
import com.baidu.ub.msoa.event.Event;
import com.baidu.ub.msoa.event.EventHandler;
import com.baidu.ub.msoa.event.EventHandlerContext;
import com.baidu.ub.msoa.utils.proto.ProtobufCodec;
import org.springframework.stereotype.Component;

/**
 * Created by pippo on 15/7/19.
 */
@Component
public class HttpRouteCodec extends ProtobufCodec implements EventHandler<RouteEvent>, RouterConstants {

    @Override
    public String getName() {
        return HttpRouteCodec.class.getSimpleName();
    }

    @Override
    public boolean accept(Event<?> event) {
        return event.getType() == EventType.HTTP_ROUTE.ordinal();
    }

    @Override
    public void downstream(EventHandlerContext context, RouteEvent event) throws Throwable {
        RouteMessage message = event.message;
        RouteMeta meta = message.meta;
        context.setAttribute(RPC_TRACE_ID, meta.traceId);
        context.setAttribute(RPC_SEQUENCE, meta.sequence);
        context.setAttribute(RPC_SIGNATURE, meta.signature);
        context.setAttribute(RPC_PROVIDER, meta.provider);
        context.setAttribute(RPC_SERVICE, meta.service);
        context.setAttribute(RPC_VERSION, meta.version);
        context.setAttribute(RPC_METHOD, meta.method);
        context.setAttribute(RPC_BODY, encode(message.getArguments()));
        context.passThrough(event);
    }

    @Override
    public void upstream(EventHandlerContext context, RouteEvent event) throws Throwable {
        RouteMessage message = event.message;
        //        RouteMeta meta = message.meta;
        //        meta.traceId = context.getAttribute(RPC_TRACE_ID);
        //        meta.sequence = context.getAttribute(RPC_SEQUENCE);
        //        meta.signature = context.getAttribute(RPC_SIGNATURE);
        //        meta.provider = context.getAttribute(RPC_PROVIDER);
        //        meta.service = context.getAttribute(RPC_SERVICE);
        //        meta.version = context.getAttribute(RPC_VERSION);
        //        meta.method = context.getAttribute(RPC_METHOD);

        byte[] body = context.getAttribute(RPC_BODY);
        Object[] results = decode(body, message.resultType);
        message.result = results.length > 0 ? results[0] : null;
    }

    @Override
    public void onException(EventHandlerContext context, RouteEvent event, Throwable throwable) {

    }

}
