package com.baidu.ub.msoa.governance;

import asia.stampy.server.message.message.MessageMessage;
import com.baidu.ub.msoa.governance.inbound.websocket.ServiceTopologySubscribeEndpoint;
import com.baidu.ub.msoa.pubusb.MessageProcessor;
import com.baidu.ub.msoa.pubusb.websocket.WebSocketSubscriber;
import org.junit.Test;

/**
 * Created by pippo on 15/8/21.
 */
public class ServiceTopologySubscribeTest {

    @Test
    public void subscribe() throws InterruptedException {

        WebSocketSubscriber subscriber =
                new WebSocketSubscriber("ws://127.0.0.1:8156" + ServiceTopologySubscribeEndpoint.PATH);
        subscriber.init();

        subscriber.subscribe("abc", new MessageProcessor() {

            @Override
            public void process(String channel, MessageMessage msg) {

            }

        });

        Thread.sleep(1000 * 60);
    }

}
