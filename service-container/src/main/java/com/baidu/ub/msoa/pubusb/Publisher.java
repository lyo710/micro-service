package com.baidu.ub.msoa.pubusb;

import asia.stampy.server.message.message.MessageMessage;

/**
 * Created by pippo on 15/8/21.
 */
public interface Publisher {

    /**
     * publish message to channel
     *
     * @param channel
     * @param message
     */
    void publish(String channel, MessageMessage message);
}
