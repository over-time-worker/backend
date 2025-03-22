package com.owlexpress.product.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    MASTER("MASTER"),
    HUB_MANAGER("HUB_MANAGER"),
    HUB_DELIVERY_MANAGER("HUB_DELIVERY_MANAGER"),
    CONSUMER_COMPANY_MANAGER("CONSUMER_COMPANY_MANAGER"),
    CONSUME_DELIVERY_MANAGER("CONSUME_DELIVERY_MANAGER"),
    PRODUCER_COMPANY_MANAGER("PRODUCER_COMPANY_MANAGER");

    private final String name;
}
