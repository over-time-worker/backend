package com.owlexpress.delivery.application.dtos.response;

import com.owlexpress.delivery.domain.entity.Delivery;
import com.owlexpress.delivery.domain.entity.Delivery.DeliveryStatus;
import com.owlexpress.delivery.domain.entity.Delivery.OrderType;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryFindResponseDto {
    private UUID orderId;
    private String productInfo;
    private UUID startHubId;
    private String startHubName;
    private UUID destinationHubId;
    private String destinationHubName;
    private UUID consumerDeliverId;
    private OrderType orderType;
    private String description;
    private LocalDateTime requestArrivalTime;
    private Duration totalEstimateDurationTime;
    private Double totalEstimateDistance;
    private DeliveryStatus deliveryStatus;
    private UUID consumerCompanyId;
    private String consumerPhoneNumber;
    private String consumerName;
    private String shippingAddress;

    @Builder
    public DeliveryFindResponseDto(
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
            String shippingAddress
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
    }

    public static DeliveryFindResponseDto toDto(Delivery delivery) {
        return DeliveryFindResponseDto.builder()
                .orderId(delivery.getOrderId())
                .productInfo(delivery.getProductInfo())
                .startHubId(delivery.getStartHubId())
                .startHubName(delivery.getStartHubName())
                .destinationHubId(delivery.getDestinationHubId())
                .destinationHubName(delivery.getDestinationHubName())
                .consumerDeliverId(delivery.getConsumerDeliverId())
                .orderType(delivery.getOrderType())
                .description(delivery.getDescription())
                .requestArrivalTime(delivery.getRequestArrivalTime())
                .totalEstimateDurationTime(delivery.getTotalEstimateDurationTime())
                .totalEstimateDistance(delivery.getTotalEstimateDistance())
                .deliveryStatus(delivery.getDeliveryStatus())
                .consumerCompanyId(delivery.getConsumerCompanyId())
                .consumerPhoneNumber(delivery.getConsumerPhoneNumber())
                .consumerName(delivery.getConsumerName())
                .shippingAddress(delivery.getShippingAddress())
                .build();
    }
}
