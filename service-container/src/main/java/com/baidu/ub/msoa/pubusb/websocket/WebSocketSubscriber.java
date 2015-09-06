package com.baidu.ub.msoa.pubusb.websocket;

import asia.stampy.server.message.message.MessageMessage;
import com.baidu.ub.msoa.pubusb.Constants;
import com.baidu.ub.msoa.pubusb.MessageProcessor;
import com.baidu.ub.msoa.pubusb.Subscriber;
import com.baidu.ub.msoa.utils.websocket.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.net.URI;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by pippo on 15/8/4.
 */
public class WebSocketSubscriber implements Subscriber, Constants, Closeable {

    private static Logger logger = LoggerFactory.getLogger(WebSocketSubscriber.class);
    private static ScheduledExecutorService daemonExecutor = Executors.newScheduledThreadPool(1);

    protected String[] addresses;
    protected SubscribeClientEndpoint endpoint;
    protected ScheduledFuture<?> daemon;

    public WebSocketSubscriber(final String... addresses) {
        this.addresses = addresses;
        this.daemon = daemonExecutor.scheduleWithFixedDelay(new Daemon(), 1, 10, TimeUnit.SECONDS);
    }

    public void init() {
        String address = addresses[new Random().nextInt(addresses.length)];
        if (endpoint == null) {
            endpoint = new SubscribeClientEndpoint();
        }

        try {
            WebSocketClient.connect(endpoint, URI.create(address));
        } catch (Exception e) {
            logger.warn("web socket:[{}] subscriber init due to error:[{}]", address, e.getMessage());
        }
    }

    @Override
    public String getId() {
        return endpoint.getId();
    }

    @Override
    public Set<String> getChannels() {
        return endpoint.getChannels();
    }

    public void subscribe(String channel, MessageProcessor processor) {
        endpoint.subscribe(channel, processor);
    }

    @Override
    public void onMessage(String channel, MessageMessage message) {
        endpoint.onMessage(channel, message);
    }

    @Override
    public void sendMessage(MessageMessage message) {
        if (endpoint.isConnected()) {
            endpoint.sendMessage(message);
        } else {
            logger.warn("can not send message:[{}], because remote endpoint is not connect", message.getHeader());
        }
    }

    @Override
    public void close() {
        daemon.cancel(true);
        endpoint.close();
    }

    private class Daemon implements Runnable {

        @Override
        public void run() {
            if (addresses == null || addresses.length == 0) {
                return;
            }

            try {
                if (!endpoint.isConnected()) {
                    init();
                    return;
                }

                long idleTime = System.currentTimeMillis() - endpoint.getActiveTime();
                if (idleTime > PING_INTERVAL) {
                    endpoint.send(PING_MSG);
                }
            } catch (Throwable e) {
                logger.warn("check daemon due to error", e);
            }
        }
    }

}
