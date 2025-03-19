package com.owlexpress.hub.domain.repository;

import java.time.Duration;
import java.time.LocalDateTime;
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
}
