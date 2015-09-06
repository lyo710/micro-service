package com.baidu.ub.msoa.container.support.rpc.outbound.event.handler;

import com.baidu.ub.msoa.container.BundleContainer;
import com.baidu.ub.msoa.container.support.governance.discover.ServiceEndpointDiscover;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint;
import com.baidu.ub.msoa.container.support.router.method.ServiceMethodNotFoundException;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCDestination;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCRequest;
import com.baidu.ub.msoa.container.support.rpc.domain.model.RPCEvent;
import com.baidu.ub.msoa.container.support.rpc.outbound.event.EventConstants;
import com.baidu.ub.msoa.event.EventHandlerAdapter;
import com.baidu.ub.msoa.event.EventHandlerContext;
import org.springframework.beans.factory.support.MethodExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by pippo on 15/9/4.
 */
@Component("rpc.outbound.ServiceDiscover")
public class ServiceDiscover extends EventHandlerAdapter<RPCEvent> implements EventConstants {

    @Override
    public void downstream(EventHandlerContext context, RPCEvent event) throws Throwable {
        RPCRequest request = event.payload.request;
        RPCDestination destination = request.header.destination;
        discover(context, destination);
        context.passThrough(event);
    }

    private void discover(EventHandlerContext context, RPCDestination destination) {
        context.setAttribute(CONTEXT_ATTR_DESTINATION, destination);

        MethodExecutor executor = null;
        Endpoint endpoint = null;

        switch (destination.routeStrategy) {
            case local:
                executor = BundleContainer.get().getBean(destination.serviceIdentity);
                if (executor == null) {
                    throw new ServiceMethodNotFoundException(String.format("can not find local service match:[%s]",
                            destination));
                }
                break;
            case remote:
                endpoint = serviceEndpointDiscover.select(destination);
                if (endpoint == null) {
                    throw new ServiceMethodNotFoundException(String.format("can not find remote service match:[%s]",
                            destination));
                }
                break;
            default:
                executor = BundleContainer.get().getBean(destination.serviceIdentity);
                endpoint = serviceEndpointDiscover.select(destination);
                if (executor == null && endpoint == null) {
                    throw new ServiceMethodNotFoundException(String.format("can not find any service match:[%s]",
                            destination));
                }
                break;
        }

        context.setAttribute(CONTEXT_ATTR_EXECUTOR, executor);
        context.setAttribute(CONTEXT_ATTR_ENDPOINT, endpoint);
    }

    @Resource(name = "serviceEndpointDiscover")
    private ServiceEndpointDiscover serviceEndpointDiscover;
}
