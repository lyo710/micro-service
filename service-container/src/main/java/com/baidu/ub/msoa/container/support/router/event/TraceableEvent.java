package com.baidu.ub.msoa.container.support.router.event;

import com.baidu.ub.msoa.event.Event;

/**
 * Created by pippo on 15/7/19.
 */
public interface TraceableEvent<T> extends Event<T> {

    /**
     * @return traceId
     */
    String getTraceId();

    /**
     * set traceId
     *
     * @param traceId
     */
    void setTraceId(String traceId);
}
