package com.baidu.ub.msoa.container.support.router.conf;

import com.baidu.ub.msoa.container.support.router.RouteStatistics;
import com.baidu.ub.msoa.container.support.router.TraceIDHolder;
import com.baidu.ub.msoa.container.support.router.TransportSelector;
import com.baidu.ub.msoa.container.support.router.http.HttpRouteCodec;
import com.baidu.ub.msoa.container.support.router.http.HttpRouter;
import com.baidu.ub.msoa.container.support.router.method.LocalMethodExecutor;
import com.baidu.ub.msoa.container.support.router.method.RemoteMethodDiscover;
import com.baidu.ub.msoa.container.support.router.websocket.WebSocketRouteCodec;
import com.baidu.ub.msoa.container.support.router.websocket.WebSocketRouter;
import com.baidu.ub.msoa.event.EventEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by pippo on 15/7/19.
 */
@Configuration
public class EventRouteEngineFactoryBean {

    private static Logger logger = LoggerFactory.getLogger(EventRouteEngineFactoryBean.class);

    private EventEngine engine = new EventEngine();

    /**
     * init EventRouteEngine
     */
    @PostConstruct
    public void init() {
        engine.getStack()
                // . add(webSocketRouter)
                // .add(webSocketRouteCodec)
                .add(httpRouter)
                .add(httpRouteCodec)
                .add(transportSelector)
                .add(remoteMethodDiscover)
                .add(localMethodExecutor)
                .add(routeStatistics)
                .add(traceIDHolder);

        logger.info("init route engine:[{}]", engine.getStack());
    }

    /**
     * @return EventRouteEngine
     */
    @Bean(name = EventEngineNames.ROUTE_EVENT_ENGINE, autowire = Autowire.NO)
    public EventEngine route() {
        return engine;
    }

    @Resource
    private WebSocketRouter webSocketRouter;

    @Resource
    private WebSocketRouteCodec webSocketRouteCodec;

    @Resource
    private HttpRouter httpRouter;

    @Resource
    private HttpRouteCodec httpRouteCodec;

    @Resource
    private TransportSelector transportSelector;

    @Resource
    private RemoteMethodDiscover remoteMethodDiscover;

    @Resource
    private LocalMethodExecutor localMethodExecutor;

    @Resource
    private RouteStatistics routeStatistics;

    @Resource
    private TraceIDHolder traceIDHolder;

}
