/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : ctu-utils
 * user created : pippo
 * date created : 2013-4-23 - 下午7:28:08
 */
package com.baidu.ub.msoa.utils.http;

/**
 * HttpRuntimeException
 *
 * @author pippo
 * @since 2013-4-23
 */
public class HttpRuntimeException extends RuntimeException {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3058392373225533751L;

    /**
     *
     */
    public HttpRuntimeException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public HttpRuntimeException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public HttpRuntimeException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public HttpRuntimeException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public HttpRuntimeException(String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

}
