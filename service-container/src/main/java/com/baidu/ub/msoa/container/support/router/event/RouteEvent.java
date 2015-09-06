package com.baidu.ub.msoa.container.support.router.event;

import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by pippo on 15/7/19.
 */
public class RouteEvent extends RPCEvent {

    public RouteEvent(RouteMessage message) {
        this(EventType.ROUTE, message);
    }

    public RouteEvent(EventType type, RouteMessage message) {
        this.type = type;
        this.message = message;
    }

    public Endpoint endpoint;

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append("type", type)
                .append("message", message)
                .toString();
    }
}


