package com.owlexpress.hub.domain.entity;

import com.owlexpress.hub.common.BaseEntity;
import com.owlexpress.hub.common.converter.DurationConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
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

    @Column(name = "duration_of_time")
    @Convert(converter = DurationConverter.class) // 수정된 DurationConverter 적용
    private Duration durationOfTime;

    @Column(name = "estimate_distance")
    private Double estimateDistance;

    @Column(name = "estimate_time")
    private Instant estimateTime; // ISO-8601 대응

    @Builder
    public HubIntervalInfo(
            UUID hubIntervalId,
            Hub startHub,
            Hub endHub,
            Duration durationOfTime,
            Double estimateDistance,
            Instant estimateTime
    ) {
        this.hubIntervalId = hubIntervalId;
        this.startHub = startHub;
        this.endHub = endHub;
        this.durationOfTime = durationOfTime;
        this.estimateDistance = estimateDistance;
        this.estimateTime = estimateTime;
    }
}
