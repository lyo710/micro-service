package com.baidu.ub.msoa.container.support.router.event;

/**
 * Created by pippo on 15/7/19.
 */
public enum EventType {

    /**
     * route event to other
     */
    ROUTE,

    /**
     * route event to other with http
     */
    HTTP_ROUTE,

    /**
     * route event to other with websocket
     */
    WEBSOCKET_ROUTE,

    /**
     * http input event
     */
    HTTP_INPUT,

    /**
     * websocket input event
     */
    WEBSOCKET_INPUT

}
