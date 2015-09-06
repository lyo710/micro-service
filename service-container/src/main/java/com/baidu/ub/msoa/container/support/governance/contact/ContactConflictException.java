package com.baidu.ub.msoa.container.support.governance.contact;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pippo on 15/8/10.
 */
public class ContactConflictException extends Exception {

    public ContactConflictException() {
    }

    public ContactConflictException(String message) {
        super(message);
        errors.add(message);
    }

    public ContactConflictException(String message, Throwable cause) {
        super(message, cause);
        errors.add(message);
    }

    public ContactConflictException(Throwable cause) {
        super(cause);
        errors.add(ExceptionUtils.getStackTrace(cause));
    }

    public ContactConflictException(String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        errors.add(message);
    }

    public ContactConflictException(List<String> errors) {
        this.errors.addAll(errors);
    }

    @Override
    public String getMessage() {
        return Joiner.on(";\n").join(errors);
    }

    private List<String> errors = new ArrayList<>();

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return String.format("ContactConflictException{'errors'=%s}", getMessage());
    }
}
