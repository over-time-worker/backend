package com.owlexpress.user.common.exception;

public abstract class AbstractUserUpdateException extends RuntimeException{
    public AbstractUserUpdateException(String message) {
        super(message);
    }
}
