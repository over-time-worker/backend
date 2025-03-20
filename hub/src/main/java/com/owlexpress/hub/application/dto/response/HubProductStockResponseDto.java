package com.owlexpress.hub.application.dto.response;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubProductStockResponseDto {

    private UUID hubProductId;
    private Long stock;

    @Builder
    public HubProductStockResponseDto(UUID hubProductId, Long stock) {
        this.hubProductId = hubProductId;
        this.stock = stock;
    }
}
