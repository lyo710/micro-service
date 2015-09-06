package com.baidu.ub.msoa.pubusb.event;

import asia.stampy.server.message.message.MessageMessage;
import com.baidu.ub.msoa.event.Event;

/**
 * Created by pippo on 15/8/4.
 */
public class MessageEvent implements Event<MessageMessage> {

    public MessageEvent() {

    }

    public MessageEvent(String sessionId, MessageMessage message) {
        this.sessionId = sessionId;
        this.message = message;
    }

    @Override
    public int getType() {
        return message != null ? message.getMessageType().ordinal() : -1;
    }

    @Override
    public MessageMessage getPayload() {
        return message;
    }

    @Override
    public void setPayload(MessageMessage message) {
        this.message = message;
    }

    private String sessionId;

    private MessageMessage message;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
