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
import lombok.Builder;
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

    @Column(name = "description", columnDefinition = "TEXT")
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

    @Builder
    public Delivery(
            UUID orderId,
            UUID startHubId,
            String startHubName,
            UUID destinationHubId,
            String destinationHubName,
            UUID consumerDeliverId,
            OrderType orderType,
            String description,
            LocalDateTime requestArrivalTime,
            DeliveryStatus deliveryStatus,
            UUID consumerId,
            String consumerPhoneNumber,
            String consumerName,
            String shippingAddress,
            Point destinationLocation,
            List<DeliveryHistory> deliveryHistories
    ) {
        this.orderId = orderId;
        this.startHubId = startHubId;
        this.startHubName = startHubName;
        this.destinationHubId = destinationHubId;
        this.destinationHubName = destinationHubName;
        this.consumerDeliverId = consumerDeliverId;
        this.orderType = orderType;
        this.description = description;
        this.requestArrivalTime = requestArrivalTime;
        this.deliveryStatus = deliveryStatus;
        this.consumerId = consumerId;
        this.consumerPhoneNumber = consumerPhoneNumber;
        this.consumerName = consumerName;
        this.shippingAddress = shippingAddress;
        this.destinationLocation = destinationLocation;
        this.deliveryHistories = deliveryHistories;
    }

    public void updateDeliverHistory(DeliveryHistory deliveryHistory) {
        this.deliveryHistories.add(deliveryHistory);
        deliveryHistory.updateDelivery(this);
    }

    @RequiredArgsConstructor
    public enum OrderType {
        NORMAL("NORMAL"),
        FRESH("FRESH");

        private final String name;
    }
    //허브 대기중, 허브 이동중, 목적지 허브 도착, 배송중, 업체이동중, 배송완료

    @RequiredArgsConstructor
    public enum DeliveryStatus {
        PENDING_AT_HUB("PENDING_AT_HUB"),
        SHIPPING_TO_HUB("SHIPPING_TO_HUB"),
        ARRIVED_AT_FINAL_HUB("ARRIVED_AT_FINAL_HUB"),
        SHIPPING("SHIPPING"),
        SHIPPING_TO_COMPANY("SHIPPING_TO_COMPANY"),
        COMPLETE("COMPLETE");

        private final String name;
    }
}
