package com.owlexpress.delivery.domain.entity.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.owlexpress.delivery.application.exceptions.DeliveryException.NotSupportedDeliveryStatusException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DeliveryStatus {
    PENDING_AT_HUB("PENDING_AT_HUB"),
    SHIPPING_TO_HUB("SHIPPING_TO_HUB"),
    ARRIVED_AT_HUB("ARRIVED_AT_HUB"),
    SHIPPING_TO_COMPANY("SHIPPING_TO_COMPANY"),
    COMPLETE("COMPLETE");

    private final String name;

    @JsonCreator
    public static DeliveryStatus getStatus(String status) {
        for(DeliveryStatus ds : DeliveryStatus.values()) {
            if(ds.name.equalsIgnoreCase(status)) {
                return ds;
            }
        }
        throw new NotSupportedDeliveryStatusException("지원하지 않는 배송 상태 입니다." + status);
    }

    public static String validateStatus(String status) {
        for(DeliveryStatus ds : DeliveryStatus.values()) {
            if(ds.name.equalsIgnoreCase(status)) {
                return ds.name;
            }
        }
        throw new NotSupportedDeliveryStatusException("지원하지 않는 배송 상태 입니다." + status);
    }
}
