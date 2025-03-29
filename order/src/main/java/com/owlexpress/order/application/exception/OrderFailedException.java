package com.owlexpress.order.application.exception;

import jakarta.persistence.EntityNotFoundException;

public class OrderFailedException extends RuntimeException {

    public OrderFailedException() {
        super("주문 중 실패 발생");
    }
}
