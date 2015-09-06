package com.baidu.ub.msoa.pubusb.event;

import asia.stampy.client.message.subscribe.SubscribeMessage;
import com.baidu.ub.msoa.event.Event;

/**
 * Created by pippo on 15/8/4.
 */
public class SubscribeEvent implements Event<SubscribeMessage> {

    public SubscribeEvent() {

    }

    public SubscribeEvent(String sessionId, SubscribeMessage message) {
        this.sessionId = sessionId;
        this.message = message;
    }

    @Override
    public int getType() {
        return message != null ? message.getMessageType().ordinal() : -1;
    }

    @Override
    public SubscribeMessage getPayload() {
        return message;
    }

    @Override
    public void setPayload(SubscribeMessage msg) {
        this.message = msg;
    }

    private String sessionId;

    private SubscribeMessage message;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
