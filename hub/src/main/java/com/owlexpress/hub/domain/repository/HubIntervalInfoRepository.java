package com.owlexpress.hub.domain.repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public interface HubIntervalInfoRepository {
    void save(
            UUID uuid,
            UUID hubId,
            UUID hubId1,
            double distance,
            Duration duration,
            LocalDateTime parse
    );
}
