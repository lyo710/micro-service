package com.baidu.ub.msoa.pubusb.event;

import asia.stampy.client.message.unsubscribe.UnsubscribeMessage;
import com.baidu.ub.msoa.event.Event;

/**
 * Created by pippo on 15/8/4.
 */
public class UnSubscribeEvent implements Event<UnsubscribeMessage> {

    public UnSubscribeEvent() {

    }

    public UnSubscribeEvent(String sessionId, UnsubscribeMessage message) {
        this.sessionId = sessionId;
        this.message = message;
    }

    @Override
    public int getType() {
        return message != null ? message.getMessageType().ordinal() : -1;
    }

    @Override
    public UnsubscribeMessage getPayload() {
        return message;
    }

    @Override
    public void setPayload(UnsubscribeMessage message) {
        this.message = message;
    }

    private String sessionId;

    private UnsubscribeMessage message;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
