package com.owlexpress.hub.application;

import com.owlexpress.hub.application.dto.request.DirectionsRequestDto;
import com.owlexpress.hub.application.dto.response.DirectionsResponseDto;
import com.owlexpress.hub.application.dto.response.HubIntervalInfoDto;
import com.owlexpress.hub.common.dto.response.RouteResponseDto;
import com.owlexpress.hub.common.exception.HubException;
import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.repository.HubIntervalInfoRepository;
import com.owlexpress.hub.domain.repository.HubRepository;
import com.owlexpress.hub.infrastructure.api.NaverDirectionsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static com.owlexpress.hub.application.dto.response.DirectionsResponseDto.RouteOption;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubPathFinder {
    private final HubIntervalInfoRepository hubIntervalInfoRepository;
    private final HubRepository hubRepository;
    private final NaverDirectionsClient naverDirectionsClient;

    // 허브 → 허브 경로 탐색
    public List<RouteResponseDto> findShortestPath(
            Hub startHub,
            Hub endHub,
            LocalDateTime departureTime
    ) {
        log.info(
                "허브 간 최단 경로 탐색 시작: startHub={}, endHub={}, departureTime={}", startHub.getHubId(), endHub.getHubId(),
                departureTime
        );
        DirectionsRequestDto requestDto = DirectionsRequestDto.fromEntity(startHub, endHub);
        return findShortestPathInternal(startHub, endHub, departureTime);
    }

    public List<RouteResponseDto> findShortestPath(
            Hub startHub,
            Double consumerLongitude,
            Double consumerLatitude,
            LocalDateTime departureTime
    ){
        log.info("소비자 경로 탐색 시작: startHub={}, consumerLocation=({}, {}), departureTime={}",
                 startHub.getHubId(), consumerLongitude, consumerLatitude, departureTime);

        // 📡 Naver Maps API를 사용하여 최적 경로 요청
        DirectionsRequestDto requestDto = DirectionsRequestDto.builder()
                .start(DirectionsRequestDto.convertPointToString(startHub.getLocation()))
                .goal(consumerLongitude + "," + consumerLatitude)
                .option("trafast")
                .build();

        log.info("Naver Maps API 호출: {}", requestDto);
        DirectionsResponseDto responseDto = naverDirectionsClient.getDrivingRoute(requestDto).block();

        try {
            if (responseDto == null || responseDto.getRoute() == null || responseDto.getRoute()
                                                                                    .isEmpty()) {
                throw new HubException.RouteNotFoundException();
            }
            List<RouteOption> routeOptions = responseDto.getRoute().get("trafast");
            if (routeOptions == null || routeOptions.isEmpty()) {
                log.warn("Naver Maps API 경로 없음: startHub={}, consumerLocation=({}, {})",
                         startHub.getHubId(), consumerLongitude, consumerLatitude);
                throw new HubException.RouteNotFoundException();
            }
            // Naver API에서 반환된 최적 경로를 RouteResponseDto로 변환
            RouteOption routeOption = routeOptions.get(0);
            List<RouteResponseDto> route = new ArrayList<>();

            route.add(RouteResponseDto.builder()
                                      .hubId(startHub.getHubId())
                                      .hubName(startHub.getName())
                                      .previousHubId(null)
                                      .estimateDistance(Double.valueOf(routeOption.getSummary().getDistance()))
                                      .estimateDurationTime(Duration.ofSeconds(routeOption.getSummary().getDuration()))
                                      .arrivalTime(departureTime.plusSeconds(routeOption.getSummary().getDuration() / 1000)) // ms -> sec 변환
                                      .build());

            log.info(" 소비자 경로 계산 완료: 최종 거리={}m, 예상 소요 시간={}ms", routeOption.getSummary().getDistance(), routeOption.getSummary().getDuration());
            return route;
        } catch (HubException.RouteNotFoundException e) {
            log.error(" Naver Maps API 응답 없음: startHub={}, consumerLocation=({}, {})", startHub.getHubId(),
                      consumerLongitude, consumerLatitude
            );
        }
        return Collections.emptyList();
    }
    //  내부 경로 탐색 메서드 (다익스트라 알고리즘 적용)
    private List<RouteResponseDto> findShortestPathInternal(
            Hub startHub,
            Hub endHub,
            LocalDateTime departureTime
    ) {
        log.info(" 경로 탐색 내부 시작: startHub={}, endHub={}, departureTime={}", startHub.getHubId(), endHub.getHubId(),
                 departureTime
        );

        Map<String, Double> distanceMap = new HashMap<>();
        Map<String, Long> durationMap = new HashMap<>();
        Map<String, LocalDateTime> arrivalTimeMap = new HashMap<>();
        Map<String, String> previousHubMap = new HashMap<>();
        Set<String> visitedHubs = new HashSet<>(); //  중복 추가 방지용 Set 추가

        PriorityQueue<Hub> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(
                hub -> distanceMap.getOrDefault(hub.getHubId()
                                                   .toString(), Double.MAX_VALUE)));

        distanceMap.put(startHub.getHubId().toString(), 0.0);
        durationMap.put(startHub.getHubId().toString(), 0L);
        arrivalTimeMap.put(startHub.getHubId().toString(), departureTime);
        priorityQueue.add(startHub);

        log.info(" 초기 설정 완료: distanceMap={}, arrivalTimeMap={}", distanceMap, arrivalTimeMap);

        while (!priorityQueue.isEmpty()) {
            Hub currentHub = priorityQueue.poll();
            if (!visitedHubs.add(currentHub.getHubId().toString())) continue; //  방문한 허브는 다시 추가하지 않음

            log.info(" 현재 허브 처리: currentHub={}", currentHub.getHubId());

            List<Object[]> intervalObjectList = hubIntervalInfoRepository.findByStartHubByObject(currentHub.getHubId());
            List<HubIntervalInfoDto> intervalList = intervalObjectList.stream()
                                                                      .map(row -> HubIntervalInfoDto.builder()
                                                                                                    .hubIntervalId((UUID) row[0])
                                                                                                    .startHubId((UUID) row[1])
                                                                                                    .endHubId((UUID) row[2])
                                                                                                    .estimateDistance(row[3] != null ? (Double) row[3] : 0.0)
                                                                                                    .durationInSeconds(row[4] != null ? ((Number) row[4]).longValue() : 0L)
                                                                                                    .estimateTime(row[5] != null ? ((Timestamp) row[5]).toLocalDateTime() : null)
                                                                                                    .build())
                                                                      .toList();

            for (HubIntervalInfoDto interval : intervalList) {
                Hub nextHub = hubRepository.findById(interval.getEndHubId()).orElseThrow(HubException.HubNotFoundException::new);
                String nextHubId = nextHub.getHubId().toString();

                double newDistance = distanceMap.get(currentHub.getHubId().toString()) + interval.getEstimateDistance();
                long newDuration = durationMap.get(currentHub.getHubId().toString()) + interval.getDurationInSeconds();
                LocalDateTime newArrivalTime = arrivalTimeMap.get(currentHub.getHubId().toString()).plusSeconds(interval.getDurationInSeconds());

                if (!distanceMap.containsKey(nextHubId) || newDistance < distanceMap.get(nextHubId)) {
                    distanceMap.put(nextHubId, newDistance);
                    durationMap.put(nextHubId, newDuration);
                    arrivalTimeMap.put(nextHubId, newArrivalTime);
                    previousHubMap.put(nextHubId, currentHub.getHubId().toString());

                    priorityQueue.add(nextHub);
                }
            }
        }

        return reconstructRoute(previousHubMap, arrivalTimeMap, distanceMap, durationMap, startHub, endHub);
    }

    // 최적 경로 재구성
    private List<RouteResponseDto> reconstructRoute(
            Map<String, String> previousHubMap,
            Map<String, LocalDateTime> arrivalTimeMap,
            Map<String, Double> distanceMap,
            Map<String, Long> durationMap,
            Hub startHub,
            Hub endHub
    ) {
        List<RouteResponseDto> route = new ArrayList<>();
        String currentHubId = endHub.getHubId().toString();

        while (currentHubId != null && previousHubMap.containsKey(currentHubId)) {
            String previousHubId = previousHubMap.get(currentHubId);

            Hub currentHub = hubRepository.findById(UUID.fromString(currentHubId))
                                          .orElse(null);
            Hub previousHub = hubRepository.findById(UUID.fromString(previousHubId))
                                           .orElse(null);

            if (currentHub == null || previousHub == null) break;

            route.add(RouteResponseDto.builder()
                                      .hubId(currentHub.getHubId())
                                      .hubName(currentHub.getName())
                                      .previousHubId(previousHub.getHubId().toString())
                                      .estimateDistance(distanceMap.get(currentHubId))
                                      .estimateDurationTime(Duration.ofSeconds(durationMap.get(currentHubId)))
                                      .arrivalTime(arrivalTimeMap.get(currentHubId))
                                      .build());

            //  루프 종료 조건 추가: startHub까지 거슬러 올라가면 종료
            if (previousHubId.equals(startHub.getHubId())) {
                break;
            }

            currentHubId = previousHubId;
        }

        //  `startHub`를 경로의 시작점으로 추가 (만약 존재하면)
        if (!route.isEmpty()) {
            route.add(0, RouteResponseDto.builder()
                                         .hubId(startHub.getHubId())
                                         .hubName(startHub.getName())
                                         .previousHubId(null) // 출발 허브이므로 이전 허브 없음
                                         .estimateDistance(0.0)
                                         .estimateDurationTime(Duration.ofSeconds(0L))
                                         .arrivalTime(arrivalTimeMap.get(startHub.getHubId().toString()))
                                         .build());
        }

        Collections.reverse(route);
        return route;
    }


    //  소비자와 가장 가까운 허브 찾기
    private Optional<Hub> findNearestHub(
            Double consumerLongitude,
            Double consumerLatitude
    ) {
        return hubRepository.findAllWithIntervals()
                            .stream()
                            .min(Comparator.comparingDouble(hub -> calculateDistance(hub.getLocation()
                                                                                        .getX(), hub.getLocation()
                                                                                                    .getY(),
                                                                                     consumerLongitude, consumerLatitude
                            )));
    }

    private double calculateDistance(
            double x1,
            double y1,
            double x2,
            double y2
    ) {
        double deltaX = x1 - x2;
        double deltaY = y1 - y2;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}