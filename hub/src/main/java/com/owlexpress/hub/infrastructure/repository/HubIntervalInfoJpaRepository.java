package com.owlexpress.hub.infrastructure.repository;

import com.owlexpress.hub.domain.entity.HubIntervalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubIntervalInfoJpaRepository extends JpaRepository<HubIntervalInfo, UUID> {
    @Modifying
    @Query(value = """
    INSERT INTO p_hub_interval_info 
    (hub_interval_id, start_hub_id, end_hub_id, estimate_distance, duration_of_time, estimate_time, created_at, modified_at) 
    VALUES (:hubIntervalId, :startHubId, :endHubId, :estimateDistance, 
            MAKE_INTERVAL(0, 0, 0, CAST(:hour AS INTEGER), CAST(:minute AS INTEGER), CAST(:second AS INTEGER)), 
            :estimateTime, NOW(), NOW())
    """, nativeQuery = true)
    void insertHubIntervalInfo(
            UUID hubIntervalId, UUID startHubId, UUID endHubId, Double estimateDistance,
            Long hour, int minute, Long second, LocalDateTime estimateTime
    );

    @Query(value = """
        SELECT hub_interval_id, start_hub_id, end_hub_id, estimate_distance, 
               EXTRACT(EPOCH FROM duration_of_time) AS durationInSeconds, 
               estimate_time
        FROM p_hub_interval_info
        WHERE start_hub_id = :startHubId
        """, nativeQuery = true)
    List<Object[]> findByStartHubByObject(@Param("startHubId") UUID startHubId);

    @Query("SELECT h.estimateDistance FROM HubIntervalInfo h " +
            "WHERE (h.startHub.hubId = :hubId AND h.endHub.hubId = :hubId1) " +
            "   OR (h.startHub.hubId = :hubId1 AND h.endHub.hubId = :hubId)")
    Optional<Double> findDistanceBetweenHubs(
            @Param("startHubId") UUID startHubId,
            @Param("endHubId") UUID endHubId
    );
}
