package com.owlexpress.hub.application.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubIntervalInfoDto {
    private UUID hubIntervalId;
    private UUID startHubId;
    private UUID endHubId;
    private Double estimateDistance;
    private Long durationInSeconds; // INTERVAL을 초 단위로 변환
    private LocalDateTime estimateTime;

    @Builder
    public HubIntervalInfoDto(
            UUID hubIntervalId,
            UUID startHubId,
            UUID endHubId,
            Double estimateDistance,
            Long durationInSeconds,
            LocalDateTime estimateTime
    ) {
        this.hubIntervalId = hubIntervalId;
        this.startHubId = startHubId;
        this.endHubId = endHubId;
        this.estimateDistance = estimateDistance;
        this.durationInSeconds = durationInSeconds;
        this.estimateTime = estimateTime;
    }
}
