package com.owlexpress.payment.application.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductType {
    FRESH("FRESH"),
    NORMAL("NORMAL");

    private final String value;
}
