package com.owlexpress.hub.application;

import com.owlexpress.hub.application.dto.request.DirectionsRequestDto;
import com.owlexpress.hub.application.dto.response.DirectionsResponseDto;
import com.owlexpress.hub.application.dto.response.DirectionsResponseDto.RouteOption;
import com.owlexpress.hub.common.dto.response.RouteResponseDto;
import com.owlexpress.hub.common.exception.HubIntervalInfoException.LocationNotExistException;
import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.repository.HubIntervalInfoRepository;
import com.owlexpress.hub.domain.repository.HubRepository;
import com.owlexpress.hub.infrastructure.api.NaverDirectionsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubDistanceService {
    private final HubRepository hubRepository;
    private final HubIntervalInfoRepository hubIntervalInfoRepository;
    private final NaverDirectionsClient naverDirectionsClient;
    private final HubPathFinder hubPathFinder;

    @Transactional
    public void calculateAllHubDistances() {
        log.info(" [START] 허브 간 거리 계산 시작");

        List<Hub> allHubs = hubRepository.findAllWithIntervals();
        log.info(" 조회된 허브 개수: {}", allHubs.size());

        for (Hub startHub : allHubs) {
            for (Hub endHub : allHubs) {
                if (startHub.equals(endHub)) {
                    log.debug(" 동일 허브 간 이동 무시: {}", startHub.getName());
                    continue;
                }

                if (startHub.getLocation() == null || endHub.getLocation() == null) {
                    log.warn("️ 허브 위치 정보가 존재하지 않습니다. startHub={}, endHub={}", startHub.getName(), endHub.getName());
                    continue;
                }

                Optional<Double> preStoredDistance = hubIntervalInfoRepository.findDistanceBetweenHubs(startHub.getHubId(), endHub.getHubId());
                if (preStoredDistance.isPresent()) {
                    log.info("찾은 데이터 거리 정보: {} → {} (거리: {}m)", startHub.getName(), endHub.getName(), preStoredDistance.get());
                    continue;
                }

                DirectionsRequestDto requestDto = DirectionsRequestDto.fromEntity(startHub, endHub);
                log.info(" API 요청 생성: startHub={}, endHub={}", startHub.getName(), endHub.getName());

                DirectionsResponseDto responseDto = naverDirectionsClient.getDrivingRoute(requestDto).block();

                if (responseDto == null || responseDto.getRoute() == null || responseDto.getRoute().isEmpty()) {
                    log.error(" API 응답이 올바르지 않습니다. startHub={}, endHub={}", startHub.getName(), endHub.getName());
                    throw new LocationNotExistException();
                }

                List<RouteOption> routeOptions = responseDto.getRoute().get("trafast");
                if (routeOptions == null || routeOptions.isEmpty()) {
                    log.warn("️ 경로 옵션이 없습니다. startHub={}, endHub={}", startHub.getName(), endHub.getName());
                    continue;
                }

                RouteOption routeOption = routeOptions.get(0);
                log.info(" 경로 데이터 확인: 거리={}m, 예상 시간={}ms", routeOption.getSummary().getDistance(), routeOption.getSummary().getDuration());

                hubIntervalInfoRepository.save(
                        UUID.randomUUID(),
                        startHub.getHubId(),
                        endHub.getHubId(),
                        routeOption.getSummary().getDistance(),
                        Duration.ofMillis(routeOption.getSummary().getDuration()),
                        LocalDateTime.parse(routeOption.getSummary().getDepartureTime())
                );
                log.info("허브 간 거리 정보 저장 완료: {} → {} (거리: {}m, 예상 시간: {}ms)", startHub.getName(), endHub.getName(), routeOption.getSummary().getDistance(), routeOption.getSummary().getDuration());
            }
        }

        log.info(" [END] 허브 간 거리 계산 완료");
    }


    @Transactional
    @Cacheable(
            value = "shortestRoutes",
            key = "'from:' + #startHubId + ':to:' + #consumerId + ':lon:' + #consumerLongitude + ':lat:' + #consumerLatitude + ':depart:' + #departureTime"
    )
    public List<RouteResponseDto> findShortestPath(
            UUID startHubId,
            UUID consumerId,
            Double consumerLongitude,
            Double consumerLatitude,
            LocalDateTime departureTime
    ) {
        log.info("[START] findShortestPath: startHubId={}, consumerId={}, lon={}, lat={}, time={}",
                 startHubId, consumerId, consumerLongitude, consumerLatitude, departureTime);

        Hub startHub = hubRepository.findById(startHubId)
                                    .orElseThrow(LocationNotExistException::new);

        List<Hub> allHubs = hubRepository.findAllWithIntervals();

        Hub nearestToConsumer = allHubs.stream()
                                       .min(Comparator.comparingDouble(hub ->
                                           hubIntervalInfoRepository.findDistanceBetweenHubs(hub.getHubId(), startHubId).orElse(Double.MAX_VALUE) +
                                           calculateDistance(hub.getLocation(), consumerLongitude, consumerLatitude)))
                                       .orElseThrow(LocationNotExistException::new);

        // 최단 허브 간 경로 (1개 또는 다중 허브 경유 가능)
        List<RouteResponseDto> fullRoute = hubPathFinder.findShortestPath(startHub, nearestToConsumer, departureTime);

        // 마지막 허브 → 소비자 위치 도착 경로
        RouteResponseDto toConsumer = findPathToConsumer(
                nearestToConsumer,
                consumerLongitude,
                consumerLatitude,
                fullRoute.get(fullRoute.size() - 1).getArrivalTime()
        );
        fullRoute.add(toConsumer);

        return fullRoute;
    }

    private RouteResponseDto findPathToConsumer(Hub startHub, Double consumerLongitude, Double consumerLatitude, LocalDateTime departureTime) {
        log.info(" Naver Maps API 호출: {} → 소비자 위치({}, {})", startHub.getName(), consumerLongitude, consumerLatitude);

        DirectionsRequestDto requestDto = DirectionsRequestDto.builder()
                                                              .start(DirectionsRequestDto.convertPointToString(startHub.getLocation()))
                                                              .goal(consumerLongitude + "," + consumerLatitude)
                                                              .option("trafast")
                                                              .build();

        DirectionsResponseDto responseDto = naverDirectionsClient.getDrivingRoute(requestDto).block();

        if (responseDto == null || responseDto.getRoute() == null || responseDto.getRoute().isEmpty()) {
            log.info("responseDto : {}",responseDto);
            log.info("responseDto route : {}", responseDto.getRoute());
            log.error(" API 응답이 올바르지 않습니다. startHub={}, Consumer({}, {})", startHub.getName(), consumerLongitude, consumerLatitude);
            throw new LocationNotExistException();

        }

        List<RouteOption> routeOptions = responseDto.getRoute().get("trafast");
        if (routeOptions == null || routeOptions.isEmpty()) {
            log.warn(" 경로 옵션이 없습니다. startHub={}, Consumer({}, {})", startHub.getName(), consumerLongitude, consumerLatitude);
            throw new LocationNotExistException();
        }

        RouteOption routeOption = routeOptions.get(0);
        double distance = routeOption.getSummary().getDistance();
        long duration = routeOption.getSummary().getDuration();
        LocalDateTime arrivalTime = departureTime.plusSeconds(duration / 1000);

        log.info(" 소비자 경로 계산 완료: 최종 거리={}m, 예상 소요 시간={}ms, 도착 시간={}", distance, duration, arrivalTime);

        return RouteResponseDto.builder()
                               .hubId(null)
                               .hubName(null)
                               .previousHubId(startHub.getHubId().toString())
                               .estimateDistance(distance)
                               .estimateDurationTime(Duration.ofSeconds(duration))
                               .arrivalTime(arrivalTime)
                               .build();
    }
    private double calculateDistance(Point hubLocation, Double consumerLongitude, Double consumerLatitude) {
        if (hubLocation == null) return Double.MAX_VALUE;
        double hubLatitude = hubLocation.getY(); // Y는 위도 (latitude)
        double hubLongitude = hubLocation.getX(); // X는 경도 (longitude)

        double deltaX = hubLongitude - consumerLongitude;
        double deltaY = hubLatitude - consumerLatitude;

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    @Transactional
    public void calculateHubDistances(Hub startHub) {
        List<Hub> allHubs = hubRepository.findAllWithIntervals();


            for (Hub endHub : allHubs) {
                if(startHub.equals(endHub)) {continue;}
                if (startHub.getLocation() == null || endHub.getLocation() == null) {
                    log.warn("️ 허브 위치 정보가 존재하지 않습니다. startHub={}, endHub={}", startHub.getName(), endHub.getName());
                    continue;
                }

                Optional<Double> preStoredDistance = hubIntervalInfoRepository.findDistanceBetweenHubs(startHub.getHubId(), endHub.getHubId());
                if (preStoredDistance.isPresent()) {
                    log.info("찾은 데이터 거리 정보: {} → {} (거리: {}m)", startHub.getName(), endHub.getName(), preStoredDistance.get());
                    continue;
                }

                DirectionsRequestDto requestDto = DirectionsRequestDto.fromEntity(startHub, endHub);
                log.info(" API 요청 생성: startHub={}, endHub={}", startHub.getName(), endHub.getName());

                DirectionsResponseDto responseDto = naverDirectionsClient.getDrivingRoute(requestDto).block();

                if (responseDto == null || responseDto.getRoute() == null || responseDto.getRoute().isEmpty()) {
                    log.error(" API 응답이 올바르지 않습니다. startHub={}, endHub={}", startHub.getName(), endHub.getName());
                    throw new LocationNotExistException();
                }

                List<RouteOption> routeOptions = responseDto.getRoute().get("trafast");
                if (routeOptions == null || routeOptions.isEmpty()) {
                    log.warn("️ 경로 옵션이 없습니다. startHub={}, endHub={}", startHub.getName(), endHub.getName());
                    continue;
                }

                RouteOption routeOption = routeOptions.get(0);
                log.info(" 경로 데이터 확인: 거리={}m, 예상 시간={}ms", routeOption.getSummary().getDistance(), routeOption.getSummary().getDuration());

                hubIntervalInfoRepository.save(
                        UUID.randomUUID(),
                        startHub.getHubId(),
                        endHub.getHubId(),
                        routeOption.getSummary().getDistance(),
                        Duration.ofMillis(routeOption.getSummary().getDuration()),
                        LocalDateTime.parse(routeOption.getSummary().getDepartureTime())
                );
                log.info("허브 간 거리 정보 저장 완료: {} → {} (거리: {}m, 예상 시간: {}ms)", startHub.getName(), endHub.getName(), routeOption.getSummary().getDistance(), routeOption.getSummary().getDuration());
            }

        log.info(" [END] 허브 간 거리 계산 완료");
    }
}