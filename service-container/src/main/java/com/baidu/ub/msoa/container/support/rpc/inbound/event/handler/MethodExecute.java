package com.baidu.ub.msoa.container.support.rpc.inbound.event.handler;

import com.baidu.ub.msoa.container.BundleContainer;
import com.baidu.ub.msoa.container.support.router.method.ServiceMethodNotFoundException;
import com.baidu.ub.msoa.container.support.rpc.domain.model.RPCEvent;
import com.baidu.ub.msoa.container.support.rpc.outbound.LocalMethodOutbound;
import com.baidu.ub.msoa.event.EventHandlerAdapter;
import com.baidu.ub.msoa.event.EventHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.MethodExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by pippo on 15/7/19.
 */
@Component("rpc.inbound.MethodExecute")
public class MethodExecute extends EventHandlerAdapter<RPCEvent> {

    private static Logger logger = LoggerFactory.getLogger(MethodExecute.class);

    @Resource
    private LocalMethodOutbound methodOutbound;

    @Override
    public void downstream(EventHandlerContext context, RPCEvent event) throws Throwable {
        String serviceIdentity = event.payload.request.header.destination.serviceIdentity;
        MethodExecutor executor = getMethodExecutor(serviceIdentity);

        if (executor == null) {
            throw new ServiceMethodNotFoundException(String.format("can not find method:[%s]", serviceIdentity));
        }

        if (logger.isTraceEnabled()) {
            logger.trace("execute method:[{}]", serviceIdentity);
        }

        event.payload.response = methodOutbound.route(executor, event.payload.request);
        context.passThrough(event);
    }

    protected MethodExecutor getMethodExecutor(String name) {
        return BundleContainer.get().getBean(name);
    }

}
