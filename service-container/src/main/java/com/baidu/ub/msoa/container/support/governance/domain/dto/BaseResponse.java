package com.baidu.ub.msoa.container.support.governance.domain.dto;

/**
 * Created by pippo on 15/8/23.
 */
public class BaseResponse {

    protected boolean success;
    protected String error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return String.format("BaseResponse{'success'=%s, 'error'=%s}", success, error);
    }
}
