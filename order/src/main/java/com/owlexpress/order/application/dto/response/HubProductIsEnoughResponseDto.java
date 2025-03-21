package com.owlexpress.order.application.dto.response;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubProductIsEnoughResponseDto {
    private UUID hubProductId;
    private boolean isEnough;

    @Builder
    public HubProductIsEnoughResponseDto(UUID hubProductId, boolean isEnough) {
        this.hubProductId = hubProductId;
        this.isEnough = isEnough;
    }
}
