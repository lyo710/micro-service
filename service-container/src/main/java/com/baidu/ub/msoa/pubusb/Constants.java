package com.baidu.ub.msoa.pubusb;

import asia.stampy.client.message.send.SendMessage;

/**
 * Created by pippo on 15/8/24.
 */
public interface Constants {

    long SESSION_TIMEOUT = 1000 * 60 * 10;
    long PING_INTERVAL = 1000 * 60 * 5;
    String PING_MSG = new SendMessage("ping", "pong").toStompMessage(true);
    String CLIENT_ADDRESS = "client.ip";

}
