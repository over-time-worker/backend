package com.owlexpress.hub.presentation.dto.response;

import com.owlexpress.hub.common.dto.response.RouteResponseDto;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Data
public class HubIntervalInfoResponse {
    private UUID startHubId; //최초 발송 허브 id
    private String startHubName; //최초 발송 허브
    private UUID destinationHubId; //최종 목적지 허브 id
    private String destinationHubName; //최종 목적지 허브
    private Double totalEstimateDistance; //전체 예측 거리
    private Duration totalEstimateDurationTime; //전체 예측 소요시간
    private List<RouteResponseDto> hubList; //허브 경로정보

    @Builder
    public HubIntervalInfoResponse(
            UUID startHubId,
            String startHubName,
            UUID destinationHubId,
            String destinationHubName,
            Double totalEstimateDistance,
            Duration totalEstimateDurationTime,
            List<RouteResponseDto> hubList
    ) {
        this.startHubId = startHubId;
        this.startHubName = startHubName;
        this.destinationHubId = destinationHubId;
        this.destinationHubName = destinationHubName;
        this.totalEstimateDistance = totalEstimateDistance;
        this.totalEstimateDurationTime = totalEstimateDurationTime;
        this.hubList = hubList;
    }

    /**
     * RouteResponseDto 리스트를 기반으로 HubIntervalInfoResponse 생성
     */
    public static HubIntervalInfoResponse fromDTO(List<RouteResponseDto> route) {
        if (route == null || route.isEmpty()) {
            throw new IllegalArgumentException("경로 데이터가 비어 있습니다.");
        }

        // 시작 허브 정보
        RouteResponseDto startHub = route.get(0);
        // 도착 허브 정보 (리스트의 마지막 요소)
        RouteResponseDto destinationHub = route.get(route.size() - 1);

        // 전체 거리 및 시간 계산
        double totalDistance = route.stream().mapToDouble(RouteResponseDto::getEstimateDistance).sum();
        long totalDuration = route.stream()
                                  .mapToLong(routeResponseDto -> routeResponseDto.getEstimateDurationTime().toSeconds())
                                  .sum();

        return HubIntervalInfoResponse.builder()
                                      .startHubId(startHub.getHubId())
                                      .startHubName(startHub.getHubName())
                                      .destinationHubId(destinationHub.getHubId())
                                      .destinationHubName(destinationHub.getHubName())
                                      .totalEstimateDistance(totalDistance)
                                      .totalEstimateDurationTime(Duration.ofMillis(totalDuration))
                                      .hubList(route)
                                      .build();
    }

}
