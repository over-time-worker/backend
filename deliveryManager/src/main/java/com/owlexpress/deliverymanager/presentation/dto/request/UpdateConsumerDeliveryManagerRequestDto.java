package com.owlexpress.deliverymanager.presentation.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateConsumerDeliveryManagerRequestDto {
    private UUID hubId;
    private Integer assignNumber;

    @Builder
    public UpdateConsumerDeliveryManagerRequestDto(
            UUID hubId,
            Integer assignNumber
    ) {
        this.hubId = hubId;
        this.assignNumber = assignNumber;
    }
}
