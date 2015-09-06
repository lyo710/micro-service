package com.baidu.ub.msoa.container.support.router.conf;

import com.baidu.ub.msoa.container.support.router.TraceIDHolder;
import com.baidu.ub.msoa.container.support.router.http.legacy.HttpInputCodec4Legacy;
import com.baidu.ub.msoa.container.support.router.method.LocalMethodExecutor;
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
 * <tt>navi 1.0</tt>服务端处理事件驱动bean配置
 * <p/>
 * * 声明注解为<code>Configuration</code>，用于服务消费者路由引擎{@link EventEngine}的初始化。
 * 特别针对<tt>Navi 1.0</tt>实现了一系列{@link EventHandler}，
 * 包括序列化、反序列化、服务方法代理本地调用等基本的响应rpc调用步骤。
 *
 * @author zhangxu
 */
@Configuration
public class EventProcessEngine4LegacyFactoryBean {

    private static Logger logger = LoggerFactory.getLogger(EventProcessEngine4LegacyFactoryBean.class);

    private EventEngine processEngine4Legacy = new EventEngine();

    /**
     * init EventProcessEngine
     */
    @PostConstruct
    public void init() {
        processEngine4Legacy.getStack()
                .add(localMethodExecutor)
                .add(httpInputCodec4Legacy)
                .add(traceIDHolder);

        logger.info("init process engine for legacy:[{}]", processEngine4Legacy.getStack());
    }

    /**
     * @return EventProcessEngine
     */
    @Bean(name = EventEngineNames.PROCESS_EVENT_ENGINE_FOR_LEGACY, autowire = Autowire.NO)
    public EventEngine process() {
        return processEngine4Legacy;
    }

    @Resource
    private HttpInputCodec4Legacy httpInputCodec4Legacy;

    @Resource
    private LocalMethodExecutor localMethodExecutor;

    @Resource
    private TraceIDHolder traceIDHolder;

}
