package com.owl_express.ai.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_ai")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Ai extends BaseEntity {

    @Id
    @Column(length = 50)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String request;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String response;

    private UUID hubDeliverId;

    private String consumerDeliverId;

    @Column(length = 50)
    private String hubDeliverPlatformId;

    @Column(length = 50)
    private String consumerDeliverPlatformId;

    @Builder
    public Ai(
            String request,
            String response,
            UUID hubDeliverId,
            String consumerDeliverId,
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
