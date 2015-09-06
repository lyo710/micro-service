package com.baidu.ub.msoa.container.support.rpc.domain.dto;

import com.google.common.base.Throwables;

/**
 * Created by pippo on 15/9/2.
 */
public class ErrorInfo {

    public ErrorInfo() {

    }

    public ErrorInfo(Throwable throwable) {
        this.exceptionType = throwable.getClass().getName();
        this.stackTrace = Throwables.getStackTraceAsString(throwable);
    }

    public ErrorInfo(String exceptionType, String stackTrace) {
        this.exceptionType = exceptionType;
        this.stackTrace = stackTrace;
    }

    public String exceptionType;
    public String stackTrace;

}
