package com.baidu.ub.msoa.container.support.rpc.outbound.event.handler;

import com.baidu.ub.msoa.container.support.governance.domain.model.topology.Endpoint;
import com.baidu.ub.msoa.container.support.router.method.ServiceMethodNotFoundException;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCDestination;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCRequest;
import com.baidu.ub.msoa.container.support.rpc.domain.dto.RPCResponse;
import com.baidu.ub.msoa.container.support.rpc.domain.model.RPCEvent;
import com.baidu.ub.msoa.container.support.rpc.outbound.HttpRESTOutbound;
import com.baidu.ub.msoa.container.support.rpc.outbound.LocalMethodOutbound;
import com.baidu.ub.msoa.container.support.rpc.outbound.event.ServiceQPSStatistics;
import com.baidu.ub.msoa.event.EventHandlerAdapter;
import com.baidu.ub.msoa.event.EventHandlerContext;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.support.MethodExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by pippo on 15/9/4.
 */
@Component("rpc.outbound.ServiceRoute")
public class ServiceRoute extends EventHandlerAdapter<RPCEvent> {

    @Resource
    private LocalMethodOutbound methodOutbound;

    @Resource
    private HttpRESTOutbound restOutbound;

    @Override
    public void downstream(EventHandlerContext context, RPCEvent event) throws Throwable {
        RPCRequest request = event.payload.request;
        event.payload.response = route(context, request);
        context.passThrough(event);
    }

    private RPCResponse route(EventHandlerContext context, RPCRequest request) {
        RPCDestination destination = request.header.destination;
        statistics(destination.serviceIdentity).count();

        MethodExecutor executor = context.getAttribute("service.executor");
        Endpoint endpoint = context.getAttribute("service.endpoint");

        switch (destination.routeStrategy) {
            case local:
                return local(executor, request);
            case remote:
                return remote(endpoint, request);
            default:
                return switchover(request, executor, endpoint);
        }
    }

    private RPCResponse local(MethodExecutor executor, RPCRequest request) {
        return methodOutbound.route(executor, request);
    }

    private RPCResponse remote(Endpoint endpoint, RPCRequest request) {
        // TODO 根据调用频次可以切换websocket
        return restOutbound.route(endpoint, request);
    }

    private RPCResponse switchover(RPCRequest request, MethodExecutor executor, Endpoint endpoint) {

        ServiceQPSStatistics statistics = statistics(request.header.destination.serviceIdentity);

        if (statistics.qps < 100) {
            /* 负载低时优先本地 */
            return localPrecedence(request, executor, endpoint);
        } else {
            /* 负载高时优先远程 */
            return remotePrecedence(request, executor, endpoint);
        }
    }

    private RPCResponse localPrecedence(RPCRequest request, MethodExecutor executor, Endpoint endpoint) {
        if (executor != null) {
            return local(executor, request);
        } else if (endpoint != null) {
            return remote(endpoint, request);
        } else {
            throw new ServiceMethodNotFoundException(String.format("can not find service match:[%s]",
                    request.header.destination));
        }
    }

    private RPCResponse remotePrecedence(RPCRequest request, MethodExecutor executor, Endpoint endpoint) {
        if (endpoint != null) {
            return remote(endpoint, request);
        } else if (executor != null) {
            return local(executor, request);
        } else {
            throw new ServiceMethodNotFoundException(String.format("can not find service match:[%s]",
                    request.header.destination));
        }
    }

    private Cache<String, ServiceQPSStatistics> cache = CacheBuilder.<String, ServiceQPSStatistics>newBuilder()
            .concurrencyLevel(Runtime.getRuntime().availableProcessors())
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .maximumSize(10000)
            .build();

    private ServiceQPSStatistics statistics(String serviceIdentity) {
        try {
            return cache.get(serviceIdentity, statisticsLoader);
        } catch (Exception e) {
            ServiceQPSStatistics statistics = new ServiceQPSStatistics();
            cache.put(serviceIdentity, statistics);
            return statistics;
        }
    }

    private Callable<ServiceQPSStatistics> statisticsLoader = new Callable<ServiceQPSStatistics>() {

        @Override
        public ServiceQPSStatistics call() throws Exception {
            return new ServiceQPSStatistics();
        }

    };

}
