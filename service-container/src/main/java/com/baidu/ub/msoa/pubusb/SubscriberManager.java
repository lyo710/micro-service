package com.baidu.ub.msoa.pubusb;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pippo on 15/8/4.
 */
public class SubscriberManager extends MessageExchange {

    protected static Map<String, Subscriber> subscribers = new ConcurrentHashMap<>();

    public Subscriber getById(String subscriberId) {
        return subscribers.get(subscriberId);
    }

    public Subscriber add(Subscriber subscriber) {
        register("_system", subscriber);
        subscribers.put(subscriber.getId(), subscriber);
        return subscriber;
    }

    public Subscriber remove(String subscriberId) {
        Subscriber subscriber = subscribers.get(subscriberId);
        if (subscriber != null) {
            deregister(subscriber);
            subscribers.remove(subscriberId);
        }
        return subscriber;
    }

    public void register(String channel, String subscriberId) {
        register(channel, getById(subscriberId));
    }

    public void deregister(String channel, String subscriberId) {
        deregister(channel, getById(subscriberId));
    }
}
