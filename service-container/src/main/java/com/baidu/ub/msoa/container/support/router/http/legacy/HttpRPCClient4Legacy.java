package com.baidu.ub.msoa.container.support.router.http.legacy;

import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint;
import com.baidu.ub.msoa.container.support.router.RPCClient;
import com.baidu.ub.msoa.container.support.router.RouterConstants;
import com.baidu.ub.msoa.container.support.router.RouterException;
import com.baidu.ub.msoa.container.support.router.event.RouteEvent;
import com.baidu.ub.msoa.container.support.router.event.RouteMessage;
import com.baidu.ub.msoa.event.DefaultEventHandlerContext;
import com.baidu.ub.msoa.event.EventHandlerContext;
import com.baidu.ub.msoa.event.EventHandlerStack;
import com.baidu.ub.msoa.utils.proto.ProtostuffReflect;

import java.util.UUID;

/**
 * Created by pippo on 15/6/25.
 */
public class HttpRPCClient4Legacy implements RouterConstants, RPCClient {

    private static HttpRouter4Legacy route = new HttpRouter4Legacy();

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

        context.setAttribute(RPC_CONTENT_TYPE, NaviLegacyConstants.NaviSerializeProtocol.PROTOBUF.getName());
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setTraceId(UUID.randomUUID().hashCode());
        requestDTO.setMethod(destination.method);
        requestDTO.setParameters(parameters);
        // requestDTO.setParamterTypes(ArgumentTypeHelper.getArgsTypeNameArray(parameters));
        context.setAttribute(RPC_BODY, ProtostuffReflect.encode(requestDTO));

        try {
            route.downstream(context, routeEvent);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        byte[] body = context.getAttribute(RPC_BODY);
        ResponseDTO responseDTO = ProtostuffReflect.decode(body, ResponseDTO.class);
        if (responseDTO.getError() != null) {
            Throwable t = responseDTO.getError().getCause();
            throw new RouterException(String.format("remote endpoint process due to error:[%s]",
                                                    t == null ? "unknown" : "omit detailed info"));
        }

        return responseDTO.getResult() != null ? (T) responseDTO.getResult() : null;
    }

}
