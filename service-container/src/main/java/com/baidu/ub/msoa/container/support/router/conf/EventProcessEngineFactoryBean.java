package com.baidu.ub.msoa.container.support.router.conf;

import com.baidu.ub.msoa.container.support.router.TraceIDHolder;
import com.baidu.ub.msoa.container.support.router.http.HttpInputCodec;
import com.baidu.ub.msoa.container.support.router.method.LocalMethodExecutor;
import com.baidu.ub.msoa.container.support.router.websocket.WebSocketInputCodec;
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
public class EventProcessEngineFactoryBean {

    private static Logger logger = LoggerFactory.getLogger(EventProcessEngineFactoryBean.class);

    private EventEngine engine = new EventEngine();

    /**
     * init EventProcessEngine
     */
    @PostConstruct
    public void init() {
        engine.getStack()
                .add(localMethodExecutor)
                // .add(webSocketInputCodec)
                .add(httpInputCodec)
                .add(traceIDHolder);

        logger.info("init process engine:[{}]", engine.getStack());
    }

    /**
     * @return EventProcessEngine
     */
    @Bean(name = EventEngineNames.PROCESS_EVENT_ENGINE, autowire = Autowire.NO)
    public EventEngine process() {
        return engine;
    }

    @Resource
    private WebSocketInputCodec webSocketInputCodec;

    @Resource
    private HttpInputCodec httpInputCodec;

    @Resource
    private LocalMethodExecutor localMethodExecutor;

    @Resource
    private TraceIDHolder traceIDHolder;

}
