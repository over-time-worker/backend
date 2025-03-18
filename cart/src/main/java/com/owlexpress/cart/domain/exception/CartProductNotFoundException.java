package com.owlexpress.cart.domain.exception;

import jakarta.persistence.EntityNotFoundException;

public class CartProductNotFoundException extends EntityNotFoundException {
    public CartProductNotFoundException(String message) {
        super(message);
    }
}
