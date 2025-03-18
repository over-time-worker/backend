package com.owlexpress.hub.application;

import com.owlexpress.hub.application.dto.request.DirectionsRequestDto;
import com.owlexpress.hub.application.dto.response.DirectionsResponseDto;
import com.owlexpress.hub.application.dto.response.DirectionsResponseDto.RouteOption;
import com.owlexpress.hub.common.exception.HubIntervalInfoException.LocationNotExistException;
import com.owlexpress.hub.common.util.DateParserUtil;
import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.entity.HubIntervalInfo;
import com.owlexpress.hub.domain.repository.HubIntervalInfoRepository;
import com.owlexpress.hub.domain.repository.HubRepository;
import com.owlexpress.hub.infrastructure.api.NaverDirectionsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubDistanceService {
    private final HubRepository hubRepository;
    private final HubIntervalInfoRepository hubIntervalInfoRepository;
    private final NaverDirectionsClient naverDirectionsClient;

    @Transactional
    public void calculateAllHubDistances() {
        List<Hub> allHubs = hubRepository.findAllWithIntervals(); // 모든 허브 조회

        for (Hub startHub : allHubs) {
            for (Hub endHub : allHubs) {
                if (startHub.equals(endHub))
                    continue; // 자기 자신 간 이동은 스킵

                if (startHub.getLocation() == null || endHub.getLocation() == null) {

                    log.info("허브 위치 정보가 존재하지 않습니다. startHub=" + startHub.getName() + ", endHub=" + endHub.getName());

                }
                // Naver Directions API 요청 DTO 생성
                DirectionsRequestDto requestDto = DirectionsRequestDto.fromEntity(
                        startHub,
                        endHub
                );

                // API 호출하여 경로 데이터 가져오기
                DirectionsResponseDto responseDto = naverDirectionsClient.getDrivingRoute(requestDto)
                                                                         .block();

                // 응답이 올바르게 왔는지 체크
                if (responseDto == null || responseDto.getRoute() == null || responseDto.getRoute()
                                                                                        .isEmpty()) {
                    throw new LocationNotExistException();
                }

                // trafast 옵션에서 데이터 가져오기
                List<RouteOption> routeOptions = responseDto.getRoute()
                                                            .get("trafast");
                if (routeOptions == null || routeOptions.isEmpty())
                    continue;

                RouteOption routeOption = routeOptions.get(0);

                // 허브 간 이동 정보 저장
                HubIntervalInfo intervalInfo = HubIntervalInfo.builder()
                                                              .startHub(startHub)
                                                              .endHub(endHub)
                                                              .estimateDistance((double) routeOption.getSummary()
                                                                                                    .getDistance())
                                                              .durationOfTime(Duration.ofMillis(routeOption.getSummary()
                                                                                                           .getDuration()))
                                                              .estimateTime(DateParserUtil.parseToInstant(routeOption.getSummary()
                                                                                                                     .getDepartureTime())) // 🛠 해결

                                                              .build();

                hubIntervalInfoRepository.save(intervalInfo);
            }
        }
    }


}