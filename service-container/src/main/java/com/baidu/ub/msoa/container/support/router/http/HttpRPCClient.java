package com.baidu.ub.msoa.container.support.router.http;

import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint;
import com.baidu.ub.msoa.container.support.router.RPCClient;
import com.baidu.ub.msoa.container.support.router.RouterConstants;
import com.baidu.ub.msoa.container.support.router.event.RouteEvent;
import com.baidu.ub.msoa.container.support.router.event.RouteMessage;
import com.baidu.ub.msoa.event.DefaultEventHandlerContext;
import com.baidu.ub.msoa.event.EventHandlerContext;
import com.baidu.ub.msoa.event.EventHandlerStack;
import com.baidu.ub.msoa.utils.proto.ProtobufCodec;

import java.util.UUID;

/**
 * Created by pippo on 15/6/25.
 */
public class HttpRPCClient extends ProtobufCodec implements RouterConstants, RPCClient {

    private static HttpRouter route = new HttpRouter();

    /**
     * send rpc call
     *
     * @param destination
     * @param returnType
     * @param parameters
     * @param <T>
     * @return response
     */
    @SuppressWarnings("unchecked")
    public <T> T send(Endpoint destination, Class<T> returnType, Object... parameters) {
        RouteEvent routeEvent = new RouteEvent(new RouteMessage());
        routeEvent.endpoint = destination;
        routeEvent.message.resultType = returnType;
        routeEvent.message.arguments = parameters;

        EventHandlerContext context = new DefaultEventHandlerContext(new EventHandlerStack());
        context.setAttribute(RPC_TRACE_ID, UUID.randomUUID().toString());
        context.setAttribute(RPC_SEQUENCE, "");
        context.setAttribute(RPC_SIGNATURE, "");
        context.setAttribute(RPC_PROVIDER, destination.provider);
        context.setAttribute(RPC_SERVICE, destination.service);
        context.setAttribute(RPC_VERSION, destination.version);
        context.setAttribute(RPC_METHOD, destination.method);
        context.setAttribute(RPC_BODY, encode(parameters));

        try {
            route.downstream(context, routeEvent);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        byte[] body = context.getAttribute(RPC_BODY);
        Object[] results = decode(body, returnType);
        return results.length > 0 ? (T) results[0] : null;
    }

}
