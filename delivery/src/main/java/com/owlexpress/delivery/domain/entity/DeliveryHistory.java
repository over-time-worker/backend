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
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

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

    @Column(name = "deliver_channer_id", length = 50)
    private String deliverChannerId;

    @RequiredArgsConstructor
    public enum PlatformType{
        SLACK("slack");

        private final String value;

        public static PlatformType getType(String type) {
            for(PlatformType pt : PlatformType.values()) {
                if(pt.value.equalsIgnoreCase(type)) {
                    return pt;
                }
            }
            throw new NotSupportedPlatformTypeException("지원하지 않는 플랫폼 타입 입니다." + type);
        }
    }

}
