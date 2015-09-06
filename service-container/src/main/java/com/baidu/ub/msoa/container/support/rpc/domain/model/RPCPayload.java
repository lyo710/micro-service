package com.baidu.ub.msoa.container.support.rpc.domain.model;

import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCRequest;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCResponse;

/**
 * Created by pippo on 15/9/1.
 */
public class RPCPayload {

    public RPCRequest request;
    public RPCResponse response;

    public RPCRequest getRequest() {
        return request;
    }

    public void setRequest(RPCRequest request) {
        this.request = request;
    }

    public RPCResponse getResponse() {
        return response;
    }

    public void setResponse(RPCResponse response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return String.format("RPCPayload{ 'request'=%s, 'response'=%s}", request, response);
    }
}
