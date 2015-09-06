package com.baidu.ub.msoa.container.support.router.websocket;

import com.baidu.ub.msoa.container.support.governance.domain.model.BundleServiceMetaInfo;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint;
import com.baidu.ub.msoa.container.support.router.RPCClient;
import com.baidu.ub.msoa.container.support.router.RouterConstants;
import com.baidu.ub.msoa.container.support.router.event.RouteEvent;
import com.baidu.ub.msoa.container.support.router.event.RouteMessage;
import com.baidu.ub.msoa.event.DefaultEventHandlerContext;
import com.baidu.ub.msoa.event.EventHandlerContext;
import com.baidu.ub.msoa.event.EventHandlerStack;
import com.baidu.ub.msoa.utils.proto.ProtobufCodec;

/**
 * Created by pippo on 15/7/1.
 */
public class WebSocketRPCClient extends ProtobufCodec implements RouterConstants, RPCClient {

    private static WebSocketRouter router = new WebSocketRouter();

    /**
     * send rpc call
     *
     * @param destination
     * @param returnType
     * @param parameters
     * @param <T>
     * @return response
     */
    public <T> T send(Endpoint destination, Class<T> returnType, Object... parameters) {

        BundleServiceMetaInfo metaInfo = destination;

        RouteEvent routeEvent = new RouteEvent(new RouteMessage());
        routeEvent.endpoint = destination;
        routeEvent.message = new RouteMessage(metaInfo.provider,
                metaInfo.service,
                metaInfo.version,
                metaInfo.method,
                metaInfo.routeStrategy);
        routeEvent.message.resultType = returnType;
        routeEvent.message.arguments = parameters;

        EventHandlerContext context = new DefaultEventHandlerContext(new EventHandlerStack());
        context.setAttribute(RPC_META, encode(routeEvent.message.meta));
        context.setAttribute(RPC_BODY, encode(routeEvent.message.arguments));

        try {
            router.downstream(context, routeEvent);
        } catch (Throwable throwable) {
            throw new RuntimeException("websocket client route due to error", throwable);
        }

        byte[] body = context.getAttribute(RPC_BODY);
        Object[] results = decode(body, returnType);
        return results.length > 0 ? (T) results[0] : null;
    }

}
