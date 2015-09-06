package com.baidu.ub.msoa.container.support.router.event;

/**
 * Created by pippo on 15/7/20.
 */
public class RPCEvent implements TraceableEvent<RouteMessage> {

    public EventType type;
    public RouteMessage message;

    @Override
    public String getTraceId() {
        return message != null ? message.meta.traceId : null;
    }

    @Override
    public void setTraceId(String traceId) {
        if (message != null) {
            message.meta.traceId = traceId;
        }
    }

    @Override
    public int getType() {
        return type.ordinal();
    }

    public void setType(EventType type) {
        this.type = type;
    }

    @Override
    public RouteMessage getPayload() {
        return this.message;
    }

    @Override
    public void setPayload(RouteMessage message) {
        this.message = message;
    }

}
