package com.baidu.ub.msoa.container.support.router.websocket;

import com.baidu.ub.msoa.container.BundleContainer;
import com.baidu.ub.msoa.container.support.router.RouterConstants;
import com.baidu.ub.msoa.container.support.router.event.EventType;
import com.baidu.ub.msoa.container.support.router.event.InputEvent;
import com.baidu.ub.msoa.container.support.router.event.RouteMessage;
import com.baidu.ub.msoa.container.support.router.method.ServiceMethodNotFoundException;
import com.baidu.ub.msoa.event.Event;
import com.baidu.ub.msoa.event.EventHandlerAdapter;
import com.baidu.ub.msoa.event.EventHandlerContext;
import org.springframework.beans.factory.support.MethodExecutor;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

/**
 * Created by pippo on 15/7/20.
 */
@Component
public class WebSocketInputCodec extends EventHandlerAdapter<InputEvent> implements RouterConstants {

    public static final String WS_PACKET = "ws.packet";

    @Override
    public boolean accept(Event<?> event) {
        return event.getType() == EventType.WEBSOCKET_INPUT.ordinal();
    }

    @Override
    public void downstream(EventHandlerContext context, InputEvent event) throws Throwable {
        Packet packet = new Packet().from(ByteBuffer.wrap(event.input));
        event.message = new RouteMessage();
        event.message.meta = packet.getRouteMeta();

        MethodExecutor executor = getLocalMethodExporter(event.message.meta.serviceIdentity);
        if (executor == null) {
            throw new ServiceMethodNotFoundException(String.format("can not find local method:[%s]", event.message));
        }

        event.message.arguments = packet.getArguments(executor.getParameterTypes());

        context.setAttribute(WS_PACKET, packet);
        context.passThrough(event);
    }

    @Override
    public void upstream(EventHandlerContext context, InputEvent event) throws Throwable {
        Packet packet = context.getAttribute(WS_PACKET);
        packet.setRelust(event.message.result);
        event.output = packet.to().array();
    }

    protected MethodExecutor getLocalMethodExporter(String name) {
        return BundleContainer.get().getBean(name);
    }

}
