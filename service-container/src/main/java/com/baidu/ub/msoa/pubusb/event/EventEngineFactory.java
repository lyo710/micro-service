package com.baidu.ub.msoa.pubusb.event;

import com.baidu.ub.msoa.event.EventEngine;
import com.baidu.ub.msoa.pubusb.event.handler.SubscribeHandler;
import com.baidu.ub.msoa.pubusb.event.handler.UnSubscribeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * Created by pippo on 15/8/21.
 */
public abstract class EventEngineFactory extends EventEngine {

    protected static Logger logger = LoggerFactory.getLogger(EventEngineFactory.class);

    @PostConstruct
    public void init() {
        getStack().add(unSubscribeHandler).add(subscribeHandler);
        logger.info("init pubsub engine:[{}]", getStack());
    }

    private SubscribeHandler subscribeHandler;

    private UnSubscribeHandler unSubscribeHandler;

    public void setSubscribeHandler(SubscribeHandler subscribeHandler) {
        this.subscribeHandler = subscribeHandler;
    }

    public void setUnSubscribeHandler(UnSubscribeHandler unSubscribeHandler) {
        this.unSubscribeHandler = unSubscribeHandler;
    }
}
