package com.owlexpress.hub.domain.service;

import com.owlexpress.hub.common.dto.response.RouteResponseDto;
import com.owlexpress.hub.domain.entity.Hub;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;

public interface HubDistanceService {

    @Transactional
    void calculateAllHubDistances();

    @Transactional
    @Cacheable(
            value = "shortestRoutes",
            key = "'from:' + #startHubId + ':to:' + #consumerId + ':lon:' + #consumerLongitude + ':lat:' + #consumerLatitude + ':depart:' + #departureTime"
    )
    List<RouteResponseDto> findShortestPath(
            UUID startHubId,
            UUID consumerId,
            Double consumerLongitude,
            Double consumerLatitude,
            LocalDateTime departureTime
    );

    @Transactional
    void calculateHubDistances(Hub startHub);
}
