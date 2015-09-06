package com.baidu.ub.msoa.container;

/**
 * Created by pippo on 15/8/20.
 */
public class ContainerException extends RuntimeException {

    public ContainerException() {
    }

    public ContainerException(String message) {
        super(message);
    }

    public ContainerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContainerException(Throwable cause) {
        super(cause);
    }

    public ContainerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
