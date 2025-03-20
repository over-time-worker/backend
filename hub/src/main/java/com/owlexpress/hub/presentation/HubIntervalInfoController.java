package com.owlexpress.hub.presentation;

import com.owlexpress.hub.application.HubDistanceService;
import com.owlexpress.hub.common.dto.response.RouteResponseDto;
import com.owlexpress.hub.presentation.dto.request.RouteRequestDto;
import com.owlexpress.hub.presentation.dto.response.HubIntervalInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hub-interval-info")
@RequiredArgsConstructor
public class HubIntervalInfoController {

    private final HubDistanceService hubDistanceService;

    // 출발지 & 도착지 기준 최적 경로 조회
    @PostMapping("/route")
    public ResponseEntity<HubIntervalInfoResponse> findOptimalRoute(@RequestBody RouteRequestDto requestDto) {
        List<RouteResponseDto> route = hubDistanceService.findShortestPath(requestDto.getStartHubId(),
                                                                           requestDto.getConsumerId(),
                                                                           requestDto.getConsumerLongitude(),
                                                                           requestDto.getConsumerLatitude(),
                                                                           requestDto.getDepartureTime()
        );
        HubIntervalInfoResponse hubIntervalInfoResponse = HubIntervalInfoResponse.fromDTO(route);

        return ResponseEntity.ok(hubIntervalInfoResponse);
    }

}