package com.owlexpress.producer.domain.entity.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompanyType {
    PRODUCER("Producer");

    private final String value;
}
