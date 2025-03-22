package com.owlexpress.payment.application.dto.response;

import java.time.Duration;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RouteInfoResponseDto {

    private UUID hubId;         // 허브 ID
    private String hubName;       // 허브 이름 (추가)
    private Double estimateDistance;      // 이동 거리 (m) → 실수형으로 변경
    private Duration estimateDurationTime;        // 예상 이동 시간 (ms) → Long으로 변경

    @Builder
    public RouteInfoResponseDto(
            UUID hubId,
            String hubName,
            Double estimateDistance,
            Duration estimateDurationTime
    ) {
        this.hubId = hubId;
        this.hubName = hubName;
        this.estimateDistance = estimateDistance;
        this.estimateDurationTime = estimateDurationTime;
    }
}
