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
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubDistanceService {
    private final HubRepository hubRepository;
    private final HubIntervalInfoRepository hubIntervalInfoRepository;
    private final NaverDirectionsClient naverDirectionsClient;

    @Transactional
    public void calculateAllHubDistances() {
        log.info("🚀 [START] 허브 간 거리 계산 시작");

        List<Hub> allHubs = hubRepository.findAllWithIntervals();
        log.info("✅ 조회된 허브 개수: {}", allHubs.size());

        for (Hub startHub : allHubs) {
            for (Hub endHub : allHubs) {
                if (startHub.equals(endHub)) {
                    log.debug("🔄 동일 허브 간 이동 무시: {}", startHub.getName());
                    continue;
                }

                if (startHub.getLocation() == null || endHub.getLocation() == null) {
                    log.warn("⚠️ 허브 위치 정보가 존재하지 않습니다. startHub={}, endHub={}", startHub.getName(), endHub.getName());
                    continue;
                }

                DirectionsRequestDto requestDto = DirectionsRequestDto.fromEntity(startHub, endHub);
                log.info("📡 API 요청 생성: startHub={}, endHub={}", startHub.getName(), endHub.getName());

                DirectionsResponseDto responseDto = naverDirectionsClient.getDrivingRoute(requestDto).block();

                if (responseDto == null || responseDto.getRoute() == null || responseDto.getRoute().isEmpty()) {
                    log.error("❌ API 응답이 올바르지 않습니다. startHub={}, endHub={}", startHub.getName(), endHub.getName());
                    throw new LocationNotExistException();
                }

                List<RouteOption> routeOptions = responseDto.getRoute().get("trafast");
                if (routeOptions == null || routeOptions.isEmpty()) {
                    log.warn("⚠️ 경로 옵션이 없습니다. startHub={}, endHub={}", startHub.getName(), endHub.getName());
                    continue;
                }

                RouteOption routeOption = routeOptions.get(0);
                log.info("🚗 경로 데이터 확인: 거리={}m, 예상 시간={}ms", routeOption.getSummary().getDistance(), routeOption.getSummary().getDuration());

                hubIntervalInfoRepository.save(
                    UUID.randomUUID(),  // Generate a new ID for each interval entry
                    startHub.getHubId(),
                    endHub.getHubId(),
                    (double) routeOption.getSummary().getDistance(),
                    Duration.ofMillis(routeOption.getSummary().getDuration()),
                    LocalDateTime.parse(routeOption.getSummary().getDepartureTime())
                );
                log.info("✅ 허브 간 거리 정보 저장 완료: {} → {} (거리: {}m, 예상 시간: {}ms)", startHub.getName(), endHub.getName(), routeOption.getSummary().getDistance(), routeOption.getSummary().getDuration());
            }
        }

        log.info("🎉 [END] 허브 간 거리 계산 완료");
    }
}