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
    public List<RouteResponseDto> findShortestPath(
            UUID startHubId,
            UUID consumerId,
            Double consumerLongitude,
            Double consumerLatitude,
            LocalDateTime departureTime
    ) {
        log.info(" [START] findShortestPath: startHubId={}, consumerId={}, consumerLongitude={}, consumerLatitude={}, departureTime={}",
                 startHubId, consumerId, consumerLongitude, consumerLatitude, departureTime);

        Hub startHub = hubRepository.findById(startHubId)
                                    .orElseThrow(LocationNotExistException::new);
        log.info(" Start hub found: {}", startHub.getName());

        Set<UUID> visitedHubIds = new HashSet<>();
        List<RouteResponseDto> fullRoute = new ArrayList<>();

        // 1 출발 허브 추가 (맨 처음)
        fullRoute.add(RouteResponseDto.builder()
                                      .hubId(startHub.getHubId())
                                      .hubName(startHub.getName())
                                      .previousHubId(null)
                                      .estimateDistance(0.0)
                                      .estimateDurationTime(Duration.ofSeconds(0L))
                                      .arrivalTime(departureTime)
                                      .build());
        visitedHubIds.add(startHub.getHubId());

        //  소비자 위치에서 가장 가까운 중앙 허브 찾기
        Hub nearestCentralHub = findNearestCentralHub(consumerLongitude, consumerLatitude)
                .orElseThrow(LocationNotExistException::new);
        log.info(" Nearest central hub found: {}", nearestCentralHub.getName());

        //  출발 허브가 스포크 허브라면 중앙 허브를 경유
        if (startHub.getParentHubId() != null) {
            log.info(" Finding first leg path: {} → {}", startHub.getName(), nearestCentralHub.getName());
            List<RouteResponseDto> firstLeg = hubPathFinder.findShortestPath(
                    startHub,
                    nearestCentralHub,
                    departureTime
            );

            for (RouteResponseDto dto : firstLeg) {
                if (!visitedHubIds.contains(dto.getHubId())) {
                    fullRoute.add(dto);
                    visitedHubIds.add(dto.getHubId());
                }
            }

            log.info(" Finding second leg path: {} → Consumer ({}, {})", nearestCentralHub.getName(), consumerLongitude, consumerLatitude);
            RouteResponseDto consumerLeg = findPathToConsumer(nearestCentralHub, consumerLongitude, consumerLatitude,
                                                              fullRoute.get(fullRoute.size() - 1).getArrivalTime());

            // 소비자 위치를 마지막으로 추가
            if (!visitedHubIds.contains(consumerLeg.getHubId())) {
                fullRoute.add(consumerLeg);
                visitedHubIds.add(consumerLeg.getHubId());
            }

        } else {
            //  출발 허브에서 소비자 위치까지 직접 최적 경로 찾기
            log.info(" Finding direct path from {} to Consumer ({}, {})", startHub.getName(), consumerLongitude, consumerLatitude);
            RouteResponseDto directConsumerLeg = findPathToConsumer(startHub, consumerLongitude, consumerLatitude, departureTime);

            // 소비자 위치를 마지막으로 추가
            if (!visitedHubIds.contains(directConsumerLeg.getHubId())) {
                fullRoute.add(directConsumerLeg);
                visitedHubIds.add(directConsumerLeg.getHubId());
            }
        }
        //  거리와 예상 시간을 하나씩 앞으로 이동
        for (int i = 0; i < fullRoute.size()-1; i++) { // 마지막 요소는 처리 대상 아님
            RouteResponseDto next = fullRoute.get(i + 1);
            RouteResponseDto current = fullRoute.get(i);

            // 다음 허브의 거리와 예상 시간을 현재 허브로 이동
            fullRoute.set(i, RouteResponseDto.builder()
                                             .hubId(current.getHubId())
                                             .hubName(current.getHubName())
                                             .previousHubId(current.getPreviousHubId())
                                             .estimateDistance(i==fullRoute.size()-1 ? 0 :next.getEstimateDistance()) //  다음 허브의 거리로 업데이트
                                             .estimateDurationTime(i==fullRoute.size()-1 ? (Duration.ofSeconds(0L)) :next.getEstimateDurationTime()) //  다음 허브의 예상 시간으로 업데이트
                                             .arrivalTime(current.getArrivalTime()) // 도착 시간은 그대로 유지
                                             .build());
        }
        // 마지막 요소(CONSUMER)는 원래 그대로 유지하되, 거리 및 시간을 0으로 설정
        int lastIndex = fullRoute.size() - 1;
        RouteResponseDto lastElement = fullRoute.get(lastIndex);
        fullRoute.set(lastIndex, RouteResponseDto.builder()
                                                 .hubId(lastElement.getHubId())
                                                 .hubName(lastElement.getHubName())
                                                 .previousHubId(lastElement.getPreviousHubId())
                                                 .estimateDistance(0.0) //  거리 0 설정
                                                 .estimateDurationTime(Duration.ofSeconds(0L))  //  예상 시간 0 설정
                                                 .arrivalTime(lastElement.getArrivalTime())
                                                 .build());

        log.info(" Full route calculated, total segments: {}", fullRoute.size());
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

    private Optional<Hub> findNearestCentralHub(Double consumerLongitude, Double consumerLatitude) {
        return hubRepository.findAllCentralHub()
                .stream()
                .filter(Objects::nonNull)
                .min(Comparator.comparingDouble(hub -> calculateDistance(hub.getLocation(), consumerLongitude, consumerLatitude)));
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