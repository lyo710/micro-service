package com.baidu.ub.msoa.container.support.rpc.domain.dto.codec;

import com.baidu.ub.msoa.container.support.rpc.RPCException;

/**
 * Created by pippo on 15/9/2.
 */
public class CodecTypeNotSupportException extends RPCException {

    public CodecTypeNotSupportException() {
    }

    public CodecTypeNotSupportException(String message) {
        super(message);
    }

    public CodecTypeNotSupportException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodecTypeNotSupportException(Throwable cause) {
        super(cause);
    }

    public CodecTypeNotSupportException(String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
