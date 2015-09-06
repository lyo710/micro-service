package com.baidu.ub.msoa.container.support.router.http.legacy;

import com.baidu.ub.msoa.container.support.router.RouterConstants;
import com.baidu.ub.msoa.container.support.router.RouterException;
import com.baidu.ub.msoa.container.support.router.event.EventType;
import com.baidu.ub.msoa.container.support.router.event.RouteEvent;
import com.baidu.ub.msoa.container.support.router.event.RouteMessage;
import com.baidu.ub.msoa.container.support.router.event.RouteMeta;
import com.baidu.ub.msoa.event.Event;
import com.baidu.ub.msoa.event.EventHandlerAdapter;
import com.baidu.ub.msoa.event.EventHandlerContext;
import com.baidu.ub.msoa.utils.IdGenerator;
import com.baidu.ub.msoa.utils.proto.ProtostuffReflect4Legacy;
import org.springframework.stereotype.Component;

/**
 * <tt>navi 1.0</tt>客户端序列化、反序列化事件handler
 *
 * @author zhangxu
 */
@Component
public class HttpRouteCodec4Legacy extends EventHandlerAdapter<RouteEvent> implements RouterConstants {

    public static final String DEFAULT_CONTENT_TYPE = NaviLegacyConstants.NaviSerializeProtocol.PROTOBUF.getName();

    @Override
    public String getName() {
        return HttpRouteCodec4Legacy.class.getSimpleName();
    }

    @Override
    public boolean accept(Event<?> event) {
        return event.getType() == EventType.ROUTE.ordinal();
    }

    @Override
    public void downstream(EventHandlerContext context, RouteEvent event) throws Throwable {
        RouteMessage message = event.message;
        RouteMeta meta = message.meta;
        context.setAttribute(RPC_CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setTraceId(meta.traceId == null ? IdGenerator.genUUID().hashCode() : meta.traceId.hashCode());
        requestDTO.setMethod(meta.method);
        requestDTO.setParameters(message.getArguments());
        requestDTO.setParamterTypes(ArgumentTypeHelper.getArgsTypeNameArray(message.getArgumentTypes()));
        context.setAttribute(RPC_BODY, ProtostuffReflect4Legacy.encode(requestDTO));
        context.passThrough(event);
    }

    @Override
    public void upstream(EventHandlerContext context, RouteEvent event) throws Throwable {
        RouteMessage message = event.message;
        byte[] body = context.getAttribute(RPC_BODY);
        ResponseDTO responseDTO = ProtostuffReflect4Legacy.decode(ResponseDTO.class, body);
        message.meta.setTraceId("" + responseDTO.getTraceId());
        if (responseDTO.getStatus() == NaviLegacyConstants.StatusCode.RPC_OK) {
            message.result = responseDTO.getResult();
        } else {
            Throwable t = null;
            if (responseDTO.getError() != null) {
                t = responseDTO.getError().getCause();
            }
            throw new RouterException(String.format("remote endpoint process due to error:[%s]",
                    t == null ? "unknown" : t.getMessage()), t);
        }
    }

}
