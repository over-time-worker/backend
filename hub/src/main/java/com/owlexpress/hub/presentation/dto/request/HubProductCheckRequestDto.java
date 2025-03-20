package com.owlexpress.hub.presentation.dto.request;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubProductCheckRequestDto {
    private UUID hubProductId;
    private Integer quantity;

    public HubProductCheckRequestDto(UUID hubProductId, Integer quantity) {
        this.hubProductId = hubProductId;
        this.quantity = quantity;
    }
}
