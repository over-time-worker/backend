package com.owlexpress.producer.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompanyType {
    PRODUCER("Producer");

    private final String value;
}
