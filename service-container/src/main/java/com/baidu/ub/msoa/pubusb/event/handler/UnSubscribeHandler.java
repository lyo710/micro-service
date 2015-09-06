package com.baidu.ub.msoa.pubusb.event.handler;

import asia.stampy.client.message.subscribe.SubscribeHeader;
import asia.stampy.client.message.unsubscribe.UnsubscribeMessage;
import com.baidu.ub.msoa.event.Event;
import com.baidu.ub.msoa.event.EventHandlerAdapter;
import com.baidu.ub.msoa.event.EventHandlerContext;
import com.baidu.ub.msoa.pubusb.SubscriberManager;
import com.baidu.ub.msoa.pubusb.event.UnSubscribeEvent;

/**
 * Created by pippo on 15/8/4.
 */
public class UnSubscribeHandler extends EventHandlerAdapter<UnSubscribeEvent> {

    @Override
    public boolean accept(Event<?> event) {
        return event instanceof UnSubscribeEvent;
    }

    @Override
    public void downstream(EventHandlerContext context, UnSubscribeEvent event) throws Throwable {
        UnsubscribeMessage unSubscribe = event.getPayload();
        String channel = unSubscribe.getHeader().getHeaderValue(SubscribeHeader.DESTINATION);
        subscriberManager.deregister(channel, event.getSessionId());
    }

    private SubscriberManager subscriberManager;

    public void setSubscriberManager(SubscriberManager subscriberManager) {
        this.subscriberManager = subscriberManager;
    }

}
