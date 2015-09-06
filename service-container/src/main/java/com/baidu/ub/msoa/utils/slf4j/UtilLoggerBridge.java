package com.baidu.ub.msoa.utils.slf4j;

import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.logging.Handler;
import java.util.logging.LogManager;

/**
 * Created by pippo on 15/7/27.
 */
public class UtilLoggerBridge {

    private static boolean bridged = false;

    // java.util.logging - bridge to slf4
    public static void bridge() {
        if (bridged) {
            return;
        }

        java.util.logging.Logger rootLogger = LogManager.getLogManager().getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            rootLogger.removeHandler(handlers[i]);
        }
        SLF4JBridgeHandler.install();

        bridged = true;
    }

}
