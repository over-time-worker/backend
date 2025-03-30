package com.owlexpress.hub.domain.entity;

import com.owlexpress.hub.common.BaseEntity;
import io.hypersistence.utils.hibernate.type.interval.PostgreSQLIntervalType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_hub_interval_info")
@SQLRestriction("deleted_at is null")
public class HubIntervalInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_interval_id")
    private UUID hubIntervalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_hub_id")
    private Hub startHub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_hub_id")
    private Hub endHub;

    @Column(name = "duration_of_time",columnDefinition = "INTERVAL")
    @Type(PostgreSQLIntervalType.class)
    private Duration durationOfTime;

    @Column(name = "estimate_distance")
    private Double estimateDistance;

    @Column(name = "estimate_time")
    private LocalDateTime estimateTime;

    @Builder
    public HubIntervalInfo(
            UUID hubIntervalId,
            Hub startHub,
            Hub endHub,
            Duration durationOfTime,
            Double estimateDistance,
            LocalDateTime estimateTime
    ) {
        this.hubIntervalId = hubIntervalId;
        this.startHub = startHub;
        this.endHub = endHub;
        this.durationOfTime = durationOfTime;
        this.estimateDistance = estimateDistance;
        this.estimateTime = estimateTime;
    }
}
