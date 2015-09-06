package com.baidu.ub.msoa.pubusb;

import asia.stampy.server.message.message.MessageMessage;

/**
 * Created by pippo on 15/8/21.
 */
public interface MessageProcessor {

    void process(String channel, MessageMessage msg);

}
