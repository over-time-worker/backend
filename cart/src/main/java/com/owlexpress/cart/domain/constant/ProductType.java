package com.owlexpress.cart.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductType {
    NOMAL("NOMAL"),
    FRESH("FRESH");

    private final String value;
}
