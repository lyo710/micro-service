package com.baidu.ub.msoa.pubusb.websocket;

import asia.stampy.client.message.subscribe.SubscribeMessage;
import asia.stampy.common.message.StampyMessage;
import asia.stampy.common.parsing.StompMessageParser;
import asia.stampy.common.parsing.UnparseableException;
import asia.stampy.server.message.message.MessageMessage;
import com.baidu.ub.msoa.pubusb.Constants;
import com.baidu.ub.msoa.pubusb.MessageProcessor;
import com.baidu.ub.msoa.pubusb.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.ClientEndpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.io.Closeable;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pippo on 15/8/21.
 */
@ClientEndpoint
public class SubscribeClientEndpoint implements Subscriber, Constants, Closeable {

    private static Logger logger = LoggerFactory.getLogger(SubscribeClientEndpoint.class);
    private long lastActiveTime;
    private Session session;
    private Map<String, MessageProcessor> processors = new ConcurrentHashMap<>();
    private LifeCycleListener lifeCycleListener;
    private StompMessageParser parser = new StompMessageParser();

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        logger.debug("open remote session:[{}]", session.getId());
        this.lastActiveTime = System.currentTimeMillis();
        this.session = session;
        this.session.setMaxIdleTimeout(SESSION_TIMEOUT);

        for (String channel : processors.keySet()) {
            subscribe(channel, processors.get(channel));
        }

        if (lifeCycleListener != null) {
            lifeCycleListener.onOpen(session, config);
        }
    }

    @OnClose
    public void onClose(Session session) {
        if (lifeCycleListener != null) {
            lifeCycleListener.onClose(session);
        }

        logger.debug("close remote session:[{}]", session.getId());
        this.session = null;
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        this.lastActiveTime = System.currentTimeMillis();

        try {
            StampyMessage stompMessage = parser.parseMessage(message);

            switch (stompMessage.getMessageType()) {
                case MESSAGE:
                    MessageMessage mm = (MessageMessage) stompMessage;
                    String channel = mm.getHeader().getDestination();
                    onMessage(channel, mm);
                    break;
                default:
                    logger.error("can not process message:[{}]", message);
                    break;
            }
        } catch (UnparseableException e) {
            logger.error("invalid message:[{}]", message);
        }
    }

    @Override
    public void onMessage(String channel, MessageMessage message) {
        MessageProcessor processor = processors.get(channel);
        if (processor != null) {
            processor.process(channel, message);
        }
    }

    @Override
    public String getId() {
        return session != null ? session.getId() : null;
    }

    @Override
    public Set<String> getChannels() {
        return new HashSet<>(processors.keySet());
    }

    @Override
    public void subscribe(String channel, MessageProcessor processor) {
        processors.put(channel, processor);

        if (!isConnected()) {
            return;
        }

        SubscribeMessage message = new SubscribeMessage(channel, UUID.randomUUID().toString());
        session.getAsyncRemote().sendText(message.toStompMessage(true));
        this.lastActiveTime = System.currentTimeMillis();
    }

    @Override
    public void sendMessage(MessageMessage message) {
        session.getAsyncRemote().sendText(message.toStompMessage(true));
        this.lastActiveTime = System.currentTimeMillis();
    }

    @Override
    public void close() {
        processors.clear();

        if (session != null) {
            try {
                session.close();
            } catch (IOException e) {
                // remove quietness
            }
            session = null;
        }
    }

    public long getActiveTime() {
        return lastActiveTime;
    }

    public boolean isConnected() {
        return session != null && session.isOpen();
    }

    public void send(String message) {
        session.getAsyncRemote().sendText(message);
        this.lastActiveTime = System.currentTimeMillis();
    }

    public void setLifeCycleListener(LifeCycleListener lifeCycleListener) {
        this.lifeCycleListener = lifeCycleListener;
    }

    public interface LifeCycleListener {

        void onOpen(Session session, EndpointConfig endpointConfig);

        void onClose(Session session);

    }
}
