package com.owlexpress.hub.infrastructure.repository;

import com.owlexpress.hub.domain.repository.HubIntervalInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubIntervalInfoRepositoryImpl implements HubIntervalInfoRepository {
    private final HubIntervalInfoJpaRepository hubIntervalInfoJpaRepository;

    @Override
    public void save(
            UUID uuid,
            UUID startHub,
            UUID endHub,
            double distance,
            Duration duration,
            LocalDateTime estimateTime
    ) {
        hubIntervalInfoJpaRepository.insertHubIntervalInfo(
                uuid,
                startHub,
                endHub,
                distance,
                duration.toHours(),
                duration.toMinutesPart(),
                duration.toSeconds(),
                estimateTime
        );
    }
}
