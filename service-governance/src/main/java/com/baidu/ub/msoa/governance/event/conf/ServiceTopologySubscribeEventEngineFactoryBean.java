package com.baidu.ub.msoa.governance.event.conf;

import com.baidu.ub.msoa.event.EventEngine;
import com.baidu.ub.msoa.event.EventHandler;
import com.baidu.ub.msoa.governance.event.handler.MessageHandler;
import com.baidu.ub.msoa.pubusb.SubscriberManager;
import com.baidu.ub.msoa.pubusb.event.handler.SubscribeHandler;
import com.baidu.ub.msoa.pubusb.event.handler.UnSubscribeHandler;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;

/**
 * Created by pippo on 15/8/21.
 */
@Configuration
public class ServiceTopologySubscribeEventEngineFactoryBean {

    @Bean(name = "serviceTopologySubscribeEventEngine")
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public EventEngine engine() {
        EventEngine engine = new EventEngine();
        engine.getStack().add(messageHandler).add(getSubscribeHandler()).add(getUnSubscribeHandler());
        return engine;
    }

    private SubscribeHandler getSubscribeHandler() {
        SubscribeHandler handler = new SubscribeHandler();
        handler.setSubscriberManager(subscriberManager);
        return handler;
    }

    private EventHandler getUnSubscribeHandler() {
        UnSubscribeHandler handler = new UnSubscribeHandler();
        handler.setSubscriberManager(subscriberManager);
        return handler;
    }

    @Resource
    private MessageHandler messageHandler;

    @Resource(name = "serviceTopologySubscriberManager")
    private SubscriberManager subscriberManager;

}
