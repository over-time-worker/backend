package com.owlexpress.order.application.dto.response;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatePaymentResponseDto {
    private UUID deliveryId;

    @Builder
    public CreatePaymentResponseDto(UUID deliveryId) {
        this.deliveryId = deliveryId;
    }
}
