package com.owlexpress.delivery.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.locationtech.jts.geom.Point;

@Getter
@Entity
@Table(name = "p_delivery")
@SQLRestriction("deleted_at is null")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseEntity {

    @Id
    @Column(name = "delivery_id", length = 50)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_id", length = 50, nullable = false)
    private UUID orderId;

    @Column(name = "start_hub_id", length = 50)
    private UUID startHubId;

    @Column(name = "start_hub_name", length = 50)
    private String startHubName;

    @Column(name = "destination_hub_id", length = 50)
    private UUID destinationHubId;

    @Column(name = "destination_hub_name", length = 50)
    private String destinationHubName;

    @Column(name = "consumer_deliver_id", length = 50)
    private UUID consumerDeliverId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false)
    private OrderType orderType;

    @Column(name = "description", columnDefinition = "TEXT", length = 50)
    private String description;

    @Column(name = "request_arrival_time")
    private LocalDateTime requestArrivalTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false)
    private DeliveryStatus deliveryStatus;

    @Column(name = "consumer_id", length = 50, nullable = false)
    private UUID consumerId;

    @Column(name = "consumer_phone_number", length = 15, nullable = false)
    private String consumerPhoneNumber;

    @Column(name = "consumer_name", length = 50, nullable = false)
    private String consumerName;

    @Column(name = "shipping_address", length = 50, nullable = false)
    private String shippingAddress;

    @Column(name = "destination_location", length = 50, columnDefinition = "GEOMETRY(Point, 4326)")
    private Point destinationLocation;

    @OneToMany(mappedBy = "delivery", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<DeliveryHistory> deliveryHistories = new ArrayList<>();

    @RequiredArgsConstructor
    public enum OrderType {
        NORMAL("일반"),
        FRESH("신선");

        private final String name;
    }

    @RequiredArgsConstructor
    public enum DeliveryStatus {
        PENDING("대기 중"),
        SHIPPING("배송 중"),
        COMPLETE("배송 완료");

        private final String name;
    }
}
