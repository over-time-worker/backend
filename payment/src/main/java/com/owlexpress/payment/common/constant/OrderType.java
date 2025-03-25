package com.owlexpress.payment.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderType {
    ROCKET("ROCKET"),
    NORMAL("NORMAL");

    private final String type;
}
