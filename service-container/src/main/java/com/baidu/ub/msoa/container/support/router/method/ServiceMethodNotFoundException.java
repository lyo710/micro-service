package com.baidu.ub.msoa.container.support.router.method;

import com.baidu.ub.msoa.container.support.router.RouterException;

/**
 * Created by pippo on 15/6/25.
 */
public class ServiceMethodNotFoundException extends RouterException {

    public ServiceMethodNotFoundException() {
    }

    public ServiceMethodNotFoundException(String message) {
        super(message);
    }

    public ServiceMethodNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceMethodNotFoundException(Throwable cause) {
        super(cause);
    }

    public ServiceMethodNotFoundException(String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
