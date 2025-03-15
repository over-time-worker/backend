package com.owlexpress.gateway.common;

import java.util.Arrays;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    MASTER("MASTER"),
    HUB_MANAGER("HUB_MANAGER"),
    HUB_DELIVERY_MANAGER("HUB_DELIVERY_MANAGER"),
    CONSUMER_COMPANY_MANAGER("CONSUMER_COMPANY_MANAGER"),
    CONSUME_DELIVERY_MANAGER("CONSUME_DELIVERY_MANAGER"),
    PRODUCER_COMPANY_MANAGER("PRODUCER_COMPANY_MANAGER");

    private final String name;

    public static Role from(String roleName) {
        return Arrays.stream(Role.values())
                .filter(role -> role.name.equals(roleName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("그런 권한은 없어요."));
    }

}
