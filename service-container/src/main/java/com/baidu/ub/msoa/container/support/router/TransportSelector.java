package com.baidu.ub.msoa.container.support.router;

import com.baidu.ub.msoa.container.support.router.event.EventType;
import com.baidu.ub.msoa.container.support.router.event.RouteEvent;
import com.baidu.ub.msoa.event.Event;
import com.baidu.ub.msoa.event.EventHandlerAdapter;
import com.baidu.ub.msoa.event.EventHandlerContext;
import org.springframework.stereotype.Component;

/**
 * Created by pippo on 15/7/20.
 */
@Component
public class TransportSelector extends EventHandlerAdapter<RouteEvent> {

    @Override
    public boolean accept(Event<?> event) {
        return event.getType() == EventType.ROUTE.ordinal();
    }

    @Override
    public void downstream(EventHandlerContext context, RouteEvent event) throws Throwable {
        event.type = upgrade(event.message.meta.serviceIdentity) ? EventType.WEBSOCKET_ROUTE : EventType.HTTP_ROUTE;
        context.passThrough(event);
    }

    @Override
    public void upstream(EventHandlerContext context, RouteEvent event) throws Throwable {
        event.type = EventType.ROUTE;
    }

    protected boolean upgrade(String rpcIdentity) {
        return RouteStatistics.getQPSStatistics(rpcIdentity).getStatistics() > 1000;
    }
}
