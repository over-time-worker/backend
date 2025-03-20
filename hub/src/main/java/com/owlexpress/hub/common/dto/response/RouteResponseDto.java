package com.owlexpress.hub.common.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RouteResponseDto {
    private UUID hubId;         // 허브 ID
    private String hubName;       // 허브 이름 (추가)
    @JsonIgnore
    private String previousHubId; // 이전 허브 ID (추가 - 경로 추적용)
    private Double estimateDistance;      // 이동 거리 (m) → 실수형으로 변경
    private Duration estimateDurationTime;        // 예상 이동 시간 (ms) → Long으로 변경
    @JsonIgnore
    private LocalDateTime arrivalTime; // 예상 도착 시간

    @Builder
    public RouteResponseDto(
            UUID hubId,
            String hubName,
            String previousHubId,
            Double estimateDistance,
            Duration estimateDurationTime,
            LocalDateTime arrivalTime
    ) {
        this.hubId = hubId;
        this.hubName = hubName;
        this.previousHubId = previousHubId;
        this.estimateDistance = estimateDistance;
        this.estimateDurationTime = estimateDurationTime;
        this.arrivalTime = arrivalTime;
    }

}