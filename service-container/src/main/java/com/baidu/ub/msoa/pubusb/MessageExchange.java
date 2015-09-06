package com.baidu.ub.msoa.pubusb;

import asia.stampy.server.message.message.MessageMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by pippo on 15/8/4.
 */
public class MessageExchange implements Publisher {

    private static Logger logger = LoggerFactory.getLogger(MessageExchange.class);
    protected static Map<String, ChannelListener> listeners = new HashMap<>();

    @Override
    public void publish(String channel, MessageMessage message) {
        getChannelListener(channel).publish(message);
    }

    public void register(String channel, Subscriber subscriber) {
        if (subscriber == null) {
            return;
        }

        logger.debug("client:[{}] subscribe channel:[{}]", subscriber.getId(), channel);
        subscriber.getChannels().add(channel);
        getChannelListener(channel).addSubscriber(subscriber);
    }

    public void deregister(String channel, Subscriber subscriber) {
        if (subscriber == null) {
            return;
        }

        subscriber.getChannels().remove(channel);
        getChannelListener(channel).removeSubscriber(subscriber);
        logger.debug("client:[{}] desubscribe channel:[{}]", subscriber.getId(), channel);
    }

    public void deregister(String channel) {
        getChannelListener(channel).clear();
    }

    public void deregister(Subscriber subscriber) {
        String[] channels = subscriber.getChannels().toArray(new String[0]);
        for (String channel : channels) {
            deregister(channel, subscriber);
        }
    }

    private ReentrantLock lock = new ReentrantLock();

    protected ChannelListener getChannelListener(String channel) {
        ChannelListener listener;

        lock.lock();
        try {
            listener = listeners.get(channel);
            if (listener == null) {
                listener = new ChannelListener(channel);
                listeners.put(channel, listener);
            }
        } finally {
            lock.unlock();
        }

        return listener;
    }

    public class ChannelListener {

        public ChannelListener(String channel) {
            this.channel = channel;
        }

        private String channel;

        private Set<Subscriber> subscribers = new CopyOnWriteArraySet<>();

        public void publish(MessageMessage message) {
            //            logger.debug("publish message:[{}] to subscribers", message);

            for (Subscriber subscriber : subscribers) {
                subscriber.onMessage(channel, message);
            }
        }

        public void addSubscriber(Subscriber subscriber) {
            subscribers.add(subscriber);
        }

        public void removeSubscriber(Subscriber subscriber) {
            subscribers.remove(subscriber);
        }

        public void clear() {
            for (Subscriber subscriber : subscribers) {
                subscriber.getChannels().remove(channel);
            }

            subscribers.clear();
        }

    }

}
