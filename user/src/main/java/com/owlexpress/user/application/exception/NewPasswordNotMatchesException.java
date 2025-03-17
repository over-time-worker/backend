package com.owlexpress.user.application.exception;

import com.owlexpress.user.common.exception.AbstractUserUpdateException;

public class NewPasswordNotMatchesException extends AbstractUserUpdateException {
    public NewPasswordNotMatchesException(String message) {
        super(message);
    }
}
