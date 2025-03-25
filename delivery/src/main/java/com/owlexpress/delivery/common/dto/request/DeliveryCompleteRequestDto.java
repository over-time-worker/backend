package com.owlexpress.delivery.common.dto.request;

import java.time.Duration;
import lombok.Getter;

@Getter
public class DeliveryCompleteRequestDto {
    private Duration actualTime;
    private Double actualDistance;
}
