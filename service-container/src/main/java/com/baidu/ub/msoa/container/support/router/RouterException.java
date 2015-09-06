package com.baidu.ub.msoa.container.support.router;

import com.baidu.ub.msoa.container.ContainerException;

/**
 * Created by pippo on 15/7/8.
 */
public class RouterException extends ContainerException {

    public RouterException() {
    }

    public RouterException(String message) {
        super(message);
    }

    public RouterException(String message, Throwable cause) {
        super(message, cause);
    }

    public RouterException(Throwable cause) {
        super(cause);
    }

    public RouterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
