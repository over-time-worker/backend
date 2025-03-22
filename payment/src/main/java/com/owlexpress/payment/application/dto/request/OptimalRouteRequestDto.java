package com.owlexpress.payment.application.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptimalRouteRequestDto {

    private UUID startHubId;
    private UUID consumerId;
    private Double consumerLongitude;
    private Double consumerLatitude;
    private LocalDateTime departureTime;

    @Builder
    public OptimalRouteRequestDto(
            UUID startHubId,
            UUID consumerId,
            Double consumerLongitude,
            Double consumerLatitude,
            LocalDateTime departureTime
    ) {
        this.startHubId = startHubId;
        this.consumerId = consumerId;
        this.consumerLongitude = consumerLongitude;
        this.consumerLatitude = consumerLatitude;
        this.departureTime = departureTime;
    }
}

