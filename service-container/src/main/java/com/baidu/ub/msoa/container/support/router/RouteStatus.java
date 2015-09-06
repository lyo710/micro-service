package com.baidu.ub.msoa.container.support.router;

/**
 * Created by pippo on 15/7/21.
 */
public enum RouteStatus {

    /**
     * route error(consumer side)
     */
    ROUTE_ERROR(-2),

    /**
     * process error(provider side)
     */
    PROCESS_ERROR(-1),

    /**
     * success
     */
    SUCCESS(0),

    /**
     * route event(consumer side)
     */
    POST(1),

    /**
     * receive message(provider side)
     */
    RECEIVE(2);

    RouteStatus(int code) {
        this.code = code;
    }

    /**
     * status code
     */
    public final int code;

    /**
     * getDefault RouteStatus with code
     *
     * @param code
     * @return RouteStatus
     */
    public static RouteStatus from(int code) {
        for (RouteStatus routeStatus : RouteStatus.values()) {
            if (routeStatus.code == code) {
                return routeStatus;
            }
        }

        throw new IllegalArgumentException(String.format("invalid code:[%s]"));
    }

}
