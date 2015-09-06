package com.baidu.ub.msoa.container.support.router.method;

import com.baidu.ub.msoa.container.support.governance.discover.ServiceEndpointDiscover;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint;
import com.baidu.ub.msoa.container.support.router.RouteStrategy;
import com.baidu.ub.msoa.container.support.router.RouterConstants;
import com.baidu.ub.msoa.container.support.router.event.EventType;
import com.baidu.ub.msoa.container.support.router.event.RouteEvent;
import com.baidu.ub.msoa.event.Event;
import com.baidu.ub.msoa.event.EventHandlerAdapter;
import com.baidu.ub.msoa.event.EventHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by pippo on 15/7/19.
 */
@Component
public class RemoteMethodDiscover extends EventHandlerAdapter<RouteEvent> implements RouterConstants {

    private static Logger logger = LoggerFactory.getLogger(RemoteMethodDiscover.class);

    @Override
    public boolean accept(Event<?> event) {
        if (event.getType() != EventType.ROUTE.ordinal()) {
            return false;
        }

        return ((RouteEvent) event).message.meta.routeStrategy != RouteStrategy.local;
    }

    @Override
    public void downstream(EventHandlerContext context, RouteEvent event) throws Throwable {
        Endpoint endpoint = serviceEndpointDiscover.select(event.message.meta);
        if (endpoint == null) {
            throw new ServiceMethodNotFoundException(String.format("can not find valid remote service endpoint:[%s]",
                    event.message.meta.serviceIdentity));
        }

        if (logger.isTraceEnabled()) {
            logger.trace("remote service invoke:[{}]", event.message);
        }

        event.endpoint = endpoint;
        context.passThrough(event);
    }

    @Resource(name = "serviceEndpointDiscover")
    private ServiceEndpointDiscover serviceEndpointDiscover;

}
