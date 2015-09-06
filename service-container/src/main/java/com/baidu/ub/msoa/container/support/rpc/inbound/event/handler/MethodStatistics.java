package com.baidu.ub.msoa.container.support.rpc.inbound.event.handler;

import com.baidu.ub.msoa.container.support.rpc.domain.model.RPCEvent;
import com.baidu.ub.msoa.event.EventHandlerAdapter;
import com.baidu.ub.msoa.event.EventHandlerContext;
import org.springframework.stereotype.Component;

/**
 * Created by pippo on 15/9/2.
 */
@Component("rpc.inbound.MethodStatistics")
public class MethodStatistics extends EventHandlerAdapter<RPCEvent> {

    @Override
    public void downstream(EventHandlerContext context, RPCEvent event) throws Throwable {

        context.passThrough(event);
    }

    @Override
    public void upstream(EventHandlerContext context, RPCEvent event) throws Throwable {

    }
}
