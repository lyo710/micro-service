package com.baidu.ub.msoa.container.support.rpc.inbound.event;

import com.baidu.ub.msoa.container.support.rpc.inbound.event.handler.EventValidator;
import com.baidu.ub.msoa.container.support.rpc.inbound.event.handler.MethodExecute;
import com.baidu.ub.msoa.container.support.rpc.inbound.event.handler.MethodStatistics;
import com.baidu.ub.msoa.container.support.rpc.inbound.event.handler.TraceHandler;
import com.baidu.ub.msoa.event.EventEngine;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by pippo on 15/9/2.
 */
@Component("rpc.inboundEventEngine")
public class InboundEventEngine extends EventEngine {

    @PostConstruct
    public void init() {
        stack.add(methodExecute).add(methodStatistics).add(traceHandler).add(eventValidator);
    }

    @Resource(name = "rpc.inbound.MethodExecute")
    private MethodExecute methodExecute;

    @Resource(name = "rpc.inbound.MethodStatistics")
    private MethodStatistics methodStatistics;

    @Resource(name = "rpc.inbound.TraceHandler")
    private TraceHandler traceHandler;

    @Resource(name = "rpc.inbound.EventValidator")
    private EventValidator eventValidator;
}
