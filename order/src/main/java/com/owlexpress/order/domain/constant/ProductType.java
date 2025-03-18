package com.owlexpress.order.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductType {
    NORMAL("NORMAL"),
    FRESH("FRESH");

    private final String value;
}
