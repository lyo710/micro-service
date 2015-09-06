/* Copyright © 2010 www.myctu.cn. All rights reserved. */
/**
 * project : ctu-utils
 * user created : pippo
 * date created : 2013-5-8 - 下午5:14:16
 */
package com.baidu.ub.msoa.utils.http;

/**
 * UnexpectedHttpStatusException
 *
 * @author pippo
 * @since 2013-5-8
 */
public class UnexpectedHttpStatusException extends RuntimeException {

    private static final long serialVersionUID = -4664410630156843862L;

    public UnexpectedHttpStatusException() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public UnexpectedHttpStatusException(String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public UnexpectedHttpStatusException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public UnexpectedHttpStatusException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public UnexpectedHttpStatusException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    public UnexpectedHttpStatusException(int status) {
        super();
        this.status = status;
    }

    public UnexpectedHttpStatusException(int status, String message) {
        super(message);
        this.status = status;
    }

    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
