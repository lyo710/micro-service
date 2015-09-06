package com.baidu.ub.msoa.pubusb.websocket;

import asia.stampy.server.message.message.MessageMessage;
import com.baidu.ub.msoa.pubusb.MessageProcessor;
import com.baidu.ub.msoa.pubusb.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.Closeable;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by pippo on 15/8/4.
 */
public class WebSocketSubscriberPartner implements Subscriber, Closeable {

    private static Logger logger = LoggerFactory.getLogger(WebSocketSubscriberPartner.class);

    public WebSocketSubscriberPartner(Session session) {
        this.session = session;
    }

    private Session session;
    private Set<String> channels = new HashSet<>();

    @Override
    public String getId() {
        return session.getId();
    }

    @Override
    public Set<String> getChannels() {
        return channels;
    }

    @Override
    public void subscribe(String channel, MessageProcessor processor) {
        throw new RuntimeException("subscriber stub not support subscribe channel");
    }

    @Override
    public void onMessage(String channel, MessageMessage message) {
        sendMessage(message);
    }

    @Override
    public void sendMessage(MessageMessage message) {
        if (logger.isDebugEnabled()) {
            logger.debug("send message:[{}] to subscriber:[{}]", message.getHeader(), session.getId());
        }

        session.getAsyncRemote().sendText(message.toStompMessage(true));

        // TODO 发送报错是否关闭session
    }

    @Override
    public void close() {
        if (session != null) {

            try {
                session.close();
            } catch (IOException e) {
                // remove quietness
            }

            session = null;
        }

        channels.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WebSocketSubscriberPartner)) {
            return false;
        }
        WebSocketSubscriberPartner that = (WebSocketSubscriberPartner) o;
        return Objects.equals(session.getId(), that.session.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(session.getId());
    }
}
