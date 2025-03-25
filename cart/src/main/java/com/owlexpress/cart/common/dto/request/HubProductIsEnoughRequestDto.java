package com.owlexpress.cart.common.dto.request;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubProductIsEnoughRequestDto {
    private UUID hubProductId;
    private Integer quantity;

    @Builder
    public HubProductIsEnoughRequestDto(UUID hubProductId, Integer quantity) {
        this.hubProductId = hubProductId;
        this.quantity = quantity;
    }
}
