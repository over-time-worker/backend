package com.owlexpress.gateway.common;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TokenStatus {
    VALID,
    EXPIRED,
    INVALID
}
