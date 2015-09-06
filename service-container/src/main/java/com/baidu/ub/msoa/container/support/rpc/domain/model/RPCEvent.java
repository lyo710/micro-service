package com.baidu.ub.msoa.container.support.rpc.domain.model;

import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCRequest;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCResponse;
import com.baidu.ub.msoa.event.Event;

/**
 * Created by pippo on 15/9/1.
 */
public class RPCEvent implements Event<RPCPayload> {

    public RPCEvent(EventType type) {
        this.type = type.ordinal();
        this.payload = new RPCPayload();
    }

    public RPCEvent(EventType type, RPCRequest request) {
        this(type);
        this.payload.request = request;
    }

    public RPCEvent(EventType type, RPCResponse response) {
        this(type);
        this.payload.response = response;
    }

    public int type;

    public RPCPayload payload = new RPCPayload();

    @Override
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public RPCPayload getPayload() {
        return payload;
    }

    @Override
    public void setPayload(RPCPayload payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return String.format("RPCEvent{ 'type'=%s, 'payload'=%s}", type, payload);
    }
}
