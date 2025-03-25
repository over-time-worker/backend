package com.owlexpress.user.common.exception;

public class NewPasswordNotMatchesException extends AbstractUserUpdateException {
    public NewPasswordNotMatchesException(String message) {
        super(message);
    }
}
