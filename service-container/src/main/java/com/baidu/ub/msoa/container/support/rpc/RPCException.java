package com.baidu.ub.msoa.container.support.rpc;

import com.baidu.ub.msoa.container.ContainerException;

/**
 * Created by pippo on 15/9/2.
 */
public class RPCException extends ContainerException {

    public RPCException() {
    }

    public RPCException(String message) {
        super(message);
    }

    public RPCException(String message, Throwable cause) {
        super(message, cause);
    }

    public RPCException(Throwable cause) {
        super(cause);
    }

    public RPCException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
