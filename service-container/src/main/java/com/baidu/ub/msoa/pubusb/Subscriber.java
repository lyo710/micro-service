package com.baidu.ub.msoa.pubusb;

import asia.stampy.server.message.message.MessageMessage;

import java.util.Set;

/**
 * Created by pippo on 15/8/4.
 */
public interface Subscriber {

    String getId();

    Set<String> getChannels();

    void subscribe(String channel, MessageProcessor processor);

    void onMessage(String channel, MessageMessage message);

    void sendMessage(MessageMessage message);
}
