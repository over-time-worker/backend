package com.owlexpress.order.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderType {
    NORMAL("NORMAL"),
    ROCKET("ROCKET");

    private final String value;
}
