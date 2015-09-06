package com.baidu.ub.msoa.container.support.rpc.outbound.event;

import com.baidu.ub.msoa.container.support.rpc.inbound.event.handler.EventValidator;
import com.baidu.ub.msoa.container.support.rpc.inbound.event.handler.TraceHandler;
import com.baidu.ub.msoa.container.support.rpc.outbound.event.handler.ServiceDiscover;
import com.baidu.ub.msoa.container.support.rpc.outbound.event.handler.ServiceRoute;
import com.baidu.ub.msoa.event.EventEngine;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by pippo on 15/9/2.
 */
@Component("rpc.outboundEventEngine")
public class OutboundEventEngine extends EventEngine {

    @PostConstruct
    public void init() {
        stack.add(serviceRoute).add(serviceDiscover).add(traceHandler).add(eventValidator);
    }

    @Resource
    private ServiceRoute serviceRoute;

    @Resource
    private ServiceDiscover serviceDiscover;

    @Resource
    private TraceHandler traceHandler;

    @Resource
    private EventValidator eventValidator;
}
