package com.baidu.ub.msoa.container.support.rpc.inbound.event.handler;

import com.baidu.ub.msoa.container.support.rpc.domain.model.RPCEvent;
import com.baidu.ub.msoa.event.EventHandlerAdapter;
import com.baidu.ub.msoa.event.EventHandlerContext;
import com.google.common.base.Verify;
import org.springframework.stereotype.Component;

/**
 * Created by pippo on 15/9/2.
 */
@Component("rpc.inbound.EventValidator")
public class EventValidator extends EventHandlerAdapter<RPCEvent> {

    @Override
    public void downstream(EventHandlerContext context, RPCEvent event) throws Throwable {
        Verify.verifyNotNull(event.payload);
        Verify.verifyNotNull(event.payload.request);
        Verify.verifyNotNull(event.payload.request.id);
        Verify.verifyNotNull(event.payload.request.header);
        Verify.verifyNotNull(event.payload.request.header.destination);
        Verify.verifyNotNull(event.payload.request.header.destination.serviceIdentity);
        Verify.verifyNotNull(event.payload.request.arguments);
        context.passThrough(event);
    }

    @Override
    public void upstream(EventHandlerContext context, RPCEvent event) throws Throwable {
        Verify.verifyNotNull(event.payload);
        Verify.verifyNotNull(event.payload.response);
        Verify.verifyNotNull(event.payload.response.id);
        Verify.verifyNotNull(event.payload.response.result);
    }
}
