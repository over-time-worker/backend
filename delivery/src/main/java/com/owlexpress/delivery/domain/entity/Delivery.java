package com.owlexpress.delivery.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.owlexpress.delivery.application.dtos.request.DeliveryCompleteRequestDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryCreateRequestDto;
import com.owlexpress.delivery.application.dtos.response.AlarmCreateResponseDto;
import com.owlexpress.delivery.application.exceptions.DeliveryException.NotSupportedDeliveryStatusException;
import com.owlexpress.delivery.application.exceptions.DeliveryException.NotSupportedOrderTypeException;
import io.hypersistence.utils.hibernate.type.interval.PostgreSQLIntervalType;
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
import java.time.Duration;
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
import org.hibernate.annotations.Type;

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

    @Column(name = "product_info", nullable = false)
    private String productInfo;

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

    @Column(name = "total_estimate_duration_time", columnDefinition = "INTERVAL")
    @Type(PostgreSQLIntervalType.class)
    private Duration totalEstimateDurationTime;

    @Column(name = "total_estimate_distance")
    private Double totalEstimateDistance;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false)
    private DeliveryStatus deliveryStatus = DeliveryStatus.PENDING_AT_HUB;

    @Column(name = "consumer_company_id", length = 50, nullable = false)
    private UUID consumerCompanyId;

    @Column(name = "consumer_phone_number", length = 15, nullable = false)
    private String consumerPhoneNumber;

    @Column(name = "consumer_name", length = 50, nullable = false)
    private String consumerName;

    @Column(name = "shipping_address", length = 50, nullable = false)
    private String shippingAddress;

    @OneToMany(mappedBy = "delivery", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<DeliveryHistory> deliveryHistories = new ArrayList<>();

    @Builder
    public Delivery(
            UUID orderId,
            String productInfo,
            UUID startHubId,
            String startHubName,
            UUID destinationHubId,
            String destinationHubName,
            UUID consumerDeliverId,
            OrderType orderType,
            String description,
            LocalDateTime requestArrivalTime,
            Duration totalEstimateDurationTime,
            Double totalEstimateDistance,
            DeliveryStatus deliveryStatus,
            UUID consumerCompanyId,
            String consumerPhoneNumber,
            String consumerName,
            String shippingAddress,
            List<DeliveryHistory> deliveryHistories
    ) {
        this.orderId = orderId;
        this.productInfo = productInfo;
        this.startHubId = startHubId;
        this.startHubName = startHubName;
        this.destinationHubId = destinationHubId;
        this.destinationHubName = destinationHubName;
        this.consumerDeliverId = consumerDeliverId;
        this.orderType = orderType;
        this.description = description;
        this.requestArrivalTime = requestArrivalTime;
        this.totalEstimateDurationTime = totalEstimateDurationTime;
        this.totalEstimateDistance = totalEstimateDistance;
        this.deliveryStatus = deliveryStatus;
        this.consumerCompanyId = consumerCompanyId;
        this.consumerPhoneNumber = consumerPhoneNumber;
        this.consumerName = consumerName;
        this.shippingAddress = shippingAddress;
        this.deliveryHistories = deliveryHistories;
    }

    public static Delivery create(
            DeliveryCreateRequestDto deliveryCreateRequestDto,
            DeliveryStatus deliveryStatus,
            Long userId
    ) {

        Delivery delivery = Delivery.builder()
                .orderId(deliveryCreateRequestDto.getOrderId())
                .productInfo(deliveryCreateRequestDto.getProductInfo())
                .startHubId(deliveryCreateRequestDto.getStartHubId())
                .startHubName(deliveryCreateRequestDto.getStartHubName())
                .destinationHubId(deliveryCreateRequestDto.getDestinationHubId())
                .destinationHubName(deliveryCreateRequestDto.getDestinationHubName())
                .orderType(deliveryCreateRequestDto.getOrderType())
                .description(deliveryCreateRequestDto.getDescription())
                .requestArrivalTime(deliveryCreateRequestDto.getRequestArrivalTime())
                .totalEstimateDurationTime(deliveryCreateRequestDto.getTotalEstimateDurationTime())
                .totalEstimateDistance(deliveryCreateRequestDto.getTotalEstimateDistance())
                .deliveryStatus(deliveryStatus)
                .consumerCompanyId(deliveryCreateRequestDto.getConsumerCompanyId())
                .consumerPhoneNumber(deliveryCreateRequestDto.getConsumerPhoneNumber())
                .consumerName(deliveryCreateRequestDto.getConsumerName())
                .shippingAddress(deliveryCreateRequestDto.getShippingAddress())
                .build();

        delivery.createdEntity(userId);
        return delivery;
    }

    public void updateDeliverHistory(DeliveryHistory deliveryHistory) {
        this.deliveryHistories.add(deliveryHistory);
        deliveryHistory.updateDelivery(this);
    }

    public void updateDeliverHistoryList(List<DeliveryHistory> deliveryHistoryList) {
        this.deliveryHistories = deliveryHistoryList;
    }

    public void updateDeliveryStatus(DeliveryStatus deliveryStatus, Long userId) {
        this.deliveryStatus = deliveryStatus;
        this.modifiedEntity(userId);
    }

    public void deleteDelivery(Long userId) {
        this.deleteEntity(userId);
    }

    public void updateCompanyDeliver(UUID consumerDeliveryId, Long userId) {
        this.consumerDeliverId = consumerDeliveryId;
        this.modifiedEntity(userId);
    }

    public void updateDeliveryHistoryStatus(
            DeliveryHistory deliveryHistory,
            DeliveryStatus deliveryStatus,
            Long userId
    ) {
        deliveryHistory.updateDeliveryStatus(deliveryStatus, userId);
    }

    public void updateDeliveryHistoryActualInfo(
            DeliveryHistory deliveryHistory,
            DeliveryStatus deliveryStatus,
            DeliveryCompleteRequestDto requestDto,
            Long userId
    ) {
        deliveryHistory.updateDeliveryHistoryActualInfo(deliveryStatus, requestDto, userId);
    }

    public void updateHubDeliverInfo(
            DeliveryHistory deliveryHistory,
            AlarmCreateResponseDto alarmCreateResponseDto,
            Long userId
    ) {
        deliveryHistory.updateDeliverInfo(alarmCreateResponseDto, userId);
    }

    public void updateCompanyDeliverInfo(
            DeliveryHistory deliveryHistory,
            AlarmCreateResponseDto alarmCreateResponseDto,
            Long userId
    ) {
        this.consumerDeliverId = alarmCreateResponseDto.getDeliverId();
        deliveryHistory.updateDeliverInfo(alarmCreateResponseDto, userId);
        modifiedEntity(userId);
    }

    @RequiredArgsConstructor
    public enum OrderType {
        ROCKET("ROCKET"),
        FRESH("FRESH");

        private final String name;

        @JsonCreator
        public static OrderType getType(String type) {
            for(OrderType ot : OrderType.values()) {
                if(ot.name.equalsIgnoreCase(type)) {
                    return ot;
                }
            }
            throw new NotSupportedOrderTypeException("지원하지 않는 주문 상태 입니다." + type);
        }
    }

    @RequiredArgsConstructor
    public enum DeliveryStatus {
        PENDING_AT_HUB("PENDING_AT_HUB"),
        SHIPPING_TO_HUB("SHIPPING_TO_HUB"),
        ARRIVED_AT_HUB("ARRIVED_AT_HUB"),
        SHIPPING_TO_COMPANY("SHIPPING_TO_COMPANY"),
        COMPLETE("COMPLETE");

        private final String name;

        @JsonCreator
        public static DeliveryStatus getStatus(String status) {
            for(DeliveryStatus ds : DeliveryStatus.values()) {
                if(ds.name.equalsIgnoreCase(status)) {
                    return ds;
                }
            }
            throw new NotSupportedDeliveryStatusException("지원하지 않는 배송 상태 입니다." + status);
        }

        public static String validateStatus(String status) {
            for(DeliveryStatus ds : DeliveryStatus.values()) {
                if(ds.name.equalsIgnoreCase(status)) {
                    return ds.name;
                }
            }
            throw new NotSupportedDeliveryStatusException("지원하지 않는 배송 상태 입니다." + status);
        }
    }
}
