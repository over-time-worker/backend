package com.owlexpress.consumer.domain.entity.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompanyType {

    CONSUMER("CONSUMER");

    private final String value;
}
