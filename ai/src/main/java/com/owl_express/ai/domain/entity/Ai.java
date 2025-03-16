package com.owl_express.ai.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_ai")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ai extends BaseEntity {

    @Id
    @Column(name = "ai_id", length = 50)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "request", columnDefinition = "TEXT", nullable = false)
    private String request;

    @Column(name = "response", columnDefinition = "TEXT", nullable = false)
    private String response;

    @Column(name = "hub_deliver_id")
    private UUID hubDeliverId;

    @Column(name = "consumer_deliver_id")
    private UUID consumerDeliverId;

    @Column(name = "hub_deliver_platform_id", length = 50)
    private String hubDeliverPlatformId;

    @Column(name = "consumer_deliver_platform_id", length = 50)
    private String consumerDeliverPlatformId;

    @Builder
    public Ai(
            String request,
            String response,
            UUID hubDeliverId,
            UUID consumerDeliverId,
            String hubDeliverPlatformId,
            String consumerDeliverPlatformId
    ) {
        this.request = request;
        this.response = response;
        this.hubDeliverId = hubDeliverId;
        this.consumerDeliverId = consumerDeliverId;
        this.hubDeliverPlatformId = hubDeliverPlatformId;
        this.consumerDeliverPlatformId = consumerDeliverPlatformId;
    }
}
