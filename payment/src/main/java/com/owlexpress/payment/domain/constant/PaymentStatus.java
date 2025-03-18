package com.owlexpress.payment.domain.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentStatus {
    SUCCESS("SUCCESS"),
    FAIL("FAIL");

    private final String status;
}
