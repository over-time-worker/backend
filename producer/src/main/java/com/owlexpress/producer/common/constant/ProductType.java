package com.owlexpress.producer.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductType {

    NORMAL("NORMAL"), FRESH("FRESH");

    private final String value;
}
