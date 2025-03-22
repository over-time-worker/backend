package com.owlexpress.payment.application.dto.response;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RouteResponseDto {

    private UUID startHubId; //최초 발송 허브 id
    private String startHubName; //최초 발송 허브
    private UUID destinationHubId; //최종 목적지 허브 id
    private String destinationHubName; //최종 목적지 허브
    private Double totalEstimateDistance; //전체 예측 거리
    private Duration totalEstimateDurationTime; //전체 예측 소요시간
    private List<RouteInfoResponseDto> hubList; //허브 경로정보

    @Builder
    public RouteResponseDto(
            UUID startHubId,
            String startHubName,
            UUID destinationHubId,
            String destinationHubName,
            Double totalEstimateDistance,
            Duration totalEstimateDurationTime,
            List<RouteInfoResponseDto> hubList
    ) {
        this.startHubId = startHubId;
        this.startHubName = startHubName;
        this.destinationHubId = destinationHubId;
        this.destinationHubName = destinationHubName;
        this.totalEstimateDistance = totalEstimateDistance;
        this.totalEstimateDurationTime = totalEstimateDurationTime;
        this.hubList = hubList;
    }

}
