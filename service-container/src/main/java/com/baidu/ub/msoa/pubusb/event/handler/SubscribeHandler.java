package com.baidu.ub.msoa.pubusb.event.handler;

import asia.stampy.client.message.subscribe.SubscribeHeader.Ack;
import asia.stampy.client.message.subscribe.SubscribeMessage;
import com.baidu.ub.msoa.event.Event;
import com.baidu.ub.msoa.event.EventHandlerAdapter;
import com.baidu.ub.msoa.event.EventHandlerContext;
import com.baidu.ub.msoa.pubusb.SubscriberManager;
import com.baidu.ub.msoa.pubusb.event.SubscribeEvent;

/**
 * Created by pippo on 15/8/4.
 */
public class SubscribeHandler extends EventHandlerAdapter<SubscribeEvent> {

    //    private static Logger logger = LoggerFactory.getLogger(SubscribeHandler.class);

    @Override
    public boolean accept(Event<?> event) {
        return event instanceof SubscribeEvent;
    }

    @Override
    public void downstream(EventHandlerContext context, SubscribeEvent event) throws Throwable {
        SubscribeMessage subscribe = event.getPayload();
        String channel = subscribe.getHeader().getDestination();
        subscriberManager.register(channel, event.getSessionId());
    }

    @Override
    public void upstream(EventHandlerContext context, SubscribeEvent event) throws Throwable {
        SubscribeMessage subscribe = event.getPayload();
        subscribe.getHeader().setAck(Ack.auto);
        event.setPayload(subscribe);
    }

    private SubscriberManager subscriberManager;

    public void setSubscriberManager(SubscriberManager subscriberManager) {
        this.subscriberManager = subscriberManager;
    }

}
