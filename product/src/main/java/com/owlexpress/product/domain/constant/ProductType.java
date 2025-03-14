package com.owlexpress.product.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductType {
    NORMAL("일반"), FRESH("신선");

    private final String name;

}
