package com.owlexpress.hub.infrastructure.repository.hubIntervalInfo;

import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.repository.HubIntervalInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    @Override
    public List<Object[]> findByStartHubByObject(UUID currentHub) {
        return hubIntervalInfoJpaRepository.findByStartHubByObject(currentHub);
    }

    @Override
    public Optional<Double> findDistanceBetweenHubs(
            UUID startHubId,
            UUID endHubId
    ) {
        return hubIntervalInfoJpaRepository.findDistanceBetweenHubs(startHubId,endHubId);
    }

    @Override
    public void deleteContainsHub(Hub hub) {
        hubIntervalInfoJpaRepository.deleteHub(hub);
    }
}
