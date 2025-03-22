package com.owlexpress.delivery.application.dtos.response;

import com.owlexpress.delivery.domain.entity.Delivery;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryCreateResponseDto {
    private UUID deliveryId;

    @Builder
    public DeliveryCreateResponseDto(UUID deliveryId) {
        this.deliveryId = deliveryId;
    }

    public static DeliveryCreateResponseDto toDto(Delivery delivery) {
        return DeliveryCreateResponseDto.builder()
                .deliveryId(delivery.getId())
                .build();
    }
}
