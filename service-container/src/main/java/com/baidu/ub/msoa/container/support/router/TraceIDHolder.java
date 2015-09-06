package com.baidu.ub.msoa.container.support.router;

import com.baidu.ub.msoa.container.support.router.event.TraceableEvent;
import com.baidu.ub.msoa.event.Event;
import com.baidu.ub.msoa.event.EventHandlerAdapter;
import com.baidu.ub.msoa.event.EventHandlerContext;
import org.springframework.stereotype.Component;

/**
 * Created by pippo on 15/7/19.
 */
@Component
public class TraceIDHolder extends EventHandlerAdapter<TraceableEvent<?>> {

    private static ThreadLocal<String> traceId = new ThreadLocal<>();

    /**
     * set traceId
     *
     * @param id
     */
    public static void setThreadTraceId(String id) {
        traceId.set(id);
    }

    /**
     * getDefault traceId
     *
     * @return
     */
    public static String getThreadTraceId() {
        return traceId.get();
    }

    @Override
    public boolean accept(Event<?> event) {
        return event instanceof TraceableEvent;
    }

    @Override
    public void downstream(EventHandlerContext context, TraceableEvent<?> event) throws Throwable {
        event.setTraceId(traceId.get());
        context.passThrough(event);
    }

    @Override
    public void upstream(EventHandlerContext context, TraceableEvent<?> event) throws Throwable {
        traceId.set(event.getTraceId());
    }
}
