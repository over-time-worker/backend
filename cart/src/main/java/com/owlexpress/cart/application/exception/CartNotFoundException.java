package com.owlexpress.cart.application.exception;

import jakarta.persistence.EntityNotFoundException;

public class CartNotFoundException extends EntityNotFoundException {
    public CartNotFoundException(String message) {
        super(message);
    }
}
