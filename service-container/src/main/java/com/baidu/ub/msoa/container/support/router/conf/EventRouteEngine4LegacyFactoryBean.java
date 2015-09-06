package com.baidu.ub.msoa.container.support.router.conf;

import com.baidu.ub.msoa.container.support.router.TraceIDHolder;
import com.baidu.ub.msoa.container.support.router.http.legacy.HttpRouteCodec4Legacy;
import com.baidu.ub.msoa.container.support.router.http.legacy.HttpRouter4Legacy;
import com.baidu.ub.msoa.container.support.router.method.LocalMethodExecutor;
import com.baidu.ub.msoa.container.support.router.method.RemoteMethodDiscover;
import com.baidu.ub.msoa.event.EventEngine;
import com.baidu.ub.msoa.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * <tt>navi 1.0</tt>客户端处理事件驱动bean配置
 * 声明注解为<code>Configuration</code>，用于服务消费者路由引擎{@link EventEngine}的初始化。
 * 特别针对<tt>Navi 1.0</tt>实现了一系列{@link EventHandler}，
 * 包括序列化、反序列化、服务查找，发起接受请求等基本的发起rpc调用步骤。
 *
 * @author zhangxu
 */
@Configuration
public class EventRouteEngine4LegacyFactoryBean {

    private static Logger LOG = LoggerFactory.getLogger(EventRouteEngineFactoryBean.class);

    private EventEngine routeEngine4Legacy = new EventEngine();

    /**
     * init EventRouteEngine
     */
    @PostConstruct
    public void init() {
        routeEngine4Legacy.getStack()
                .add(httpRouter4Legacy)
                .add(httpRouteCodec4Legacy)
                .add(remoteMethodDiscover)
                .add(localMethodExecutor)
                .add(traceIDHolder);

        LOG.info("init route engine for legacy:[{}]", routeEngine4Legacy.getStack());
    }

    /**
     * @return EventRouteEngine
     */
    @Bean(name = EventEngineNames.ROUTE_EVENT_ENGINE_FOR_LEGACY,
            autowire = Autowire.NO)
    public EventEngine route() {
        return routeEngine4Legacy;
    }

    @Resource
    private HttpRouter4Legacy httpRouter4Legacy;

    @Resource
    private HttpRouteCodec4Legacy httpRouteCodec4Legacy;

    @Resource
    private RemoteMethodDiscover remoteMethodDiscover;

    @Resource
    private LocalMethodExecutor localMethodExecutor;

    @Resource
    private TraceIDHolder traceIDHolder;

}
