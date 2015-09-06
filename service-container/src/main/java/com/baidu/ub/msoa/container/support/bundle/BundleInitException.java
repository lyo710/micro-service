package com.baidu.ub.msoa.container.support.bundle;

import com.baidu.ub.msoa.container.ContainerException;

/**
 * Created by pippo on 15/7/14.
 */
public class BundleInitException extends ContainerException {

    public BundleInitException() {
    }

    public BundleInitException(String message) {
        super(message);
    }

    public BundleInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public BundleInitException(Throwable cause) {
        super(cause);
    }

    public BundleInitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
