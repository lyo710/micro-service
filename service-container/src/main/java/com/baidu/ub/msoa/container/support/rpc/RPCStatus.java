package com.baidu.ub.msoa.container.support.rpc;

/**
 * Created by pippo on 15/9/5.
 */
public enum RPCStatus {

    SUCCESS(200),

    ROUTE_ERROR(400),

    INNER_ERROR(500),

    UNKNOWN(-1);

    RPCStatus(int code) {
        this.code = code;
    }

    public static RPCStatus valueOf(int code) {
        switch (code) {
            case 200:
                return SUCCESS;
            case 400:
                return ROUTE_ERROR;
            case 500:
                return INNER_ERROR;
            default:
                return UNKNOWN;
        }
    }

    public final int code;

}
