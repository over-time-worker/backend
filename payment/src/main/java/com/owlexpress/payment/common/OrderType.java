package com.owlexpress.payment.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderType {
    ROCKET("ROCKET"),
    NORMAL("NORMAL");

    private final String type;
}
