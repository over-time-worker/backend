package com.owlexpress.deliverymanager.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlatformType {
    SLACK("slack");

    private final String name;
}
