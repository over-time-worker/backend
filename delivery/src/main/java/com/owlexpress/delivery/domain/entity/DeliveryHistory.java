package com.owlexpress.delivery.domain.entity;

import com.owlexpress.delivery.application.exceptions.DeliveryException.NotSupportedPlatformTypeException;
import com.owlexpress.delivery.domain.entity.Delivery.DeliveryStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.StringUtils;

@Getter
@Entity
@Table(name = "p_delivery_history")
@SQLRestriction("deleted_at is null")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "delivery_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Delivery delivery;

    @Column(name = "sequence", columnDefinition = "SMALLINT", nullable = false)
    private Integer sequence;

    @Column(name = "start_hub_id", length = 50)
    private UUID startHubId;

    @Column(name = "start_hub_name", length = 50)
    private String startHubName;

    @Column(name = "destination_hub_id", length = 50)
    private UUID destinationHubId;

    @Column(name = "destination_hub_name", length = 50)
    private String destinationHubName;

    @Column(name = "shipping_address", length = 50)
    private String shippingAddress;

    @Column(name = "estimate_distance", nullable = false)
    private Double estimateDistance;

    @Column(name = "estimate_time", columnDefinition = "INTERVAL", nullable = false)
    private Duration estimateTime;

    @Column(name = "actual_distance")
    private Double actualDistance;

    @Column(name = "actual_time", columnDefinition = "INTERVAL", nullable = false)
    private Duration actualTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false)
    private DeliveryStatus deliveryStatus;

    @Column(name = "deliver_id", length = 50)
    private UUID deliverId;

    @Column(name = "deliver_phone_number", length = 15)
    private String deliverPhoneNumber;

    @Column(name = "deliver_name", length = 50)
    private String deliverName;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform_type")
    private PlatformType platformType;

    @Column(name = "deliver_channel_id", length = 50)
    private String deliverChannelId;

    @Builder
    public DeliveryHistory(
            Delivery delivery,
            Integer sequence,
            UUID startHubId,
            String startHubName,
            UUID destinationHubId,
            String destinationHubName,
            String shippingAddress,
            Double estimateDistance,
            Duration estimateTime,
            Double actualDistance,
            Duration actualTime,
            DeliveryStatus deliveryStatus,
            UUID deliverId,
            String deliverPhoneNumber,
            String deliverName,
            PlatformType platformType,
            String deliverChannelId
    ){
        this.delivery = delivery;
        this.sequence = sequence;
        this.startHubId = startHubId;
        this.startHubName = startHubName;
        this.destinationHubId = destinationHubId;
        this.destinationHubName = destinationHubName;
        this.shippingAddress = shippingAddress;
        this.estimateDistance = estimateDistance;
        this.estimateTime = estimateTime;
        this.actualDistance = actualDistance;
        this.actualTime = actualTime;
        this.deliveryStatus = deliveryStatus;
        this.deliverId = deliverId;
        this.deliverPhoneNumber = deliverPhoneNumber;
        this.deliverName = deliverName;
        this.platformType = platformType;
        this.deliverChannelId = deliverChannelId;

    }

    public void updateDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    @RequiredArgsConstructor
    public enum PlatformType{
        SLACK("slack");

        private final String value;

        public static PlatformType getType(String type) {
            if(!StringUtils.hasText(type)) {
                throw new NotSupportedPlatformTypeException("플랫폼 타입이 비어있습니다.");
            }

            return Arrays.stream(PlatformType.values())
                    .filter(val -> val.name().equalsIgnoreCase(type.trim()))
                    .findFirst()
                    .orElseThrow(() -> new NotSupportedPlatformTypeException("지원하지 않는 플랫폼 타입 입니다. : " + type));
        }
    }

}
