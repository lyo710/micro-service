package com.baidu.ub.msoa.container.support.rpc.outbound.event.handler;

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
@Component("rpc.outbound.TraceHandler")
public class TraceHandler extends EventHandlerAdapter<RPCEvent> {

    @Override
    public void downstream(EventHandlerContext context, RPCEvent event) throws Throwable {
        RPCTrace trace = ThreadTrace.get();

        if (trace == null) {
            /* 没有就创建一个 */
            trace = new RPCTrace();
            trace.traceId = UUID.randomUUID().toString();
            trace.spanId = 0;
            trace.user = -1;
            trace.timestamp = System.currentTimeMillis();
            ThreadTrace.set(trace);
        } else {
            /* 否则让spanId自增 */
            trace.spanId++;
        }

        event.payload.request.header.trace = trace;
        context.passThrough(event);
    }
}
