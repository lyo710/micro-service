package com.baidu.ub.msoa.container.support.rpc.inbound;

import com.baidu.ub.msoa.container.support.rpc.RPCException;

/**
 * Created by pippo on 15/9/5.
 */
public class InvalidRestURIException extends RPCException {

    public InvalidRestURIException() {
    }

    public InvalidRestURIException(String message) {
        super(message);
    }

    public InvalidRestURIException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRestURIException(Throwable cause) {
        super(cause);
    }

    public InvalidRestURIException(String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
