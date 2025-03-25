package com.owlexpress.delivery.domain.entity.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.owlexpress.delivery.application.exceptions.DeliveryException.NotSupportedOrderTypeException;
import com.owlexpress.delivery.domain.entity.Delivery;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderType {
    ROCKET("ROCKET"),
    NORMAL("NORMAL");

    private final String name;

    @JsonCreator
    public static OrderType getType(String type) {
        for(OrderType ot : OrderType.values()) {
            if(ot.name.equalsIgnoreCase(type)) {
                return ot;
            }
        }
        throw new NotSupportedOrderTypeException("지원하지 않는 주문 상태 입니다." + type);
    }
}