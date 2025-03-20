package com.owlexpress.hub.domain.repository;

import com.owlexpress.hub.domain.entity.Hub;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubIntervalInfoRepository {
    void save(
            UUID uuid,
            UUID startHub,
            UUID endHub,
            double distance,
            Duration duration,
            LocalDateTime estimateTime
    );

    List<Object[]> findByStartHubByObject(UUID currentHub);

    Optional<Double> findDistanceBetweenHubs(
            UUID startHub,
            UUID endHub
    );

    void deleteContainsHub(Hub hub);
}
