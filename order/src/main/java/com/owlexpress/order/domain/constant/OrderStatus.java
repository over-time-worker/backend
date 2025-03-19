package com.owlexpress.order.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {
    PENDING("PENDING"),
    COMPLETE("COMPLETE"),
    CANCEL("CANCEL");

    private final String value;
}
