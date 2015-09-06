package com.baidu.ub.msoa.container.support.rpc.inbound.event.handler;

import com.baidu.ub.msoa.container.support.rpc.ThreadTrace;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCTrace;
import com.baidu.ub.msoa.container.support.rpc.domain.model.RPCEvent;
import com.baidu.ub.msoa.event.EventHandlerAdapter;
import com.baidu.ub.msoa.event.EventHandlerContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by pippo on 15/9/2.
 */
@Component("rpc.inbound.TraceHandler")
public class TraceHandler extends EventHandlerAdapter<RPCEvent> {

    @Override
    public void downstream(EventHandlerContext context, RPCEvent event) throws Throwable {
        RPCTrace trace = event.payload.request.header.trace;

        if (trace == null) {
            trace = new RPCTrace();
            trace.traceId = UUID.randomUUID().toString();
            trace.spanId = 0;
            trace.user = -1;
            trace.timestamp = System.currentTimeMillis();
            event.payload.request.header.trace = trace;
        }

        ThreadTrace.set(trace);
        context.passThrough(event);
    }
}
