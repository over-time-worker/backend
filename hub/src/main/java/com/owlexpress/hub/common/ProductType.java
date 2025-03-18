package com.owlexpress.hub.common;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProductType {
    NORMAL("NORMAL"),
    FRESH("FRESH");

    private final String type;
}
