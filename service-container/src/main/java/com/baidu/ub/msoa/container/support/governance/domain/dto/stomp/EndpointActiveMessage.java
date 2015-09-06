package com.baidu.ub.msoa.container.support.governance.domain.dto.stomp;

import asia.stampy.server.message.message.MessageMessage;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint;
import com.baidu.ub.msoa.container.support.governance.domain.model.topology.EndpointStatus;

import java.util.UUID;

/**
 * Created by pippo on 15/8/22.
 */
public class EndpointActiveMessage extends MessageMessage {

    public EndpointActiveMessage() {
        super("endpoint", UUID.randomUUID().toString(), EndpointStatus.online.name());
    }

    public EndpointActiveMessage(Endpoint endpoint) {
        this();
        setEndpoint(endpoint);
    }

    public void setEndpoint(Endpoint endpoint) {
        setBody(BodyCodec.encodeBase64(endpoint));
    }

    public Endpoint getEndpoint() {
        return BodyCodec.decodeBase64((String) getBody(), Endpoint.class);
    }

}
