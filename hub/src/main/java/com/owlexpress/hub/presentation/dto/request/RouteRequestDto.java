package com.owlexpress.hub.presentation.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RouteRequestDto {

    private UUID startHubId;

    private UUID consumerId;

    private Double consumerLongitude;

    private Double consumerLatitude;

    private LocalDateTime departureTime;

    @Builder
    public RouteRequestDto(
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
