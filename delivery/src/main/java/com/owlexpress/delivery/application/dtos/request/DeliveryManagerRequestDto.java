package com.owlexpress.delivery.application.dtos.request;

import com.owlexpress.delivery.domain.entity.Delivery;
import com.owlexpress.delivery.domain.entity.DeliveryHistory;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryManagerRequestDto {
    private UUID deliveryId;
    private UUID orderId;
    private String productInfo;
    private String ordererName;
    private String startHubName;
    private String endHubName;
    private UUID currentHubId;
    private String currentHubName;
    private UUID nextHubId;
    private String nextHubName;
    private String orderDescription;
    private String shippingAddress;
    private LocalDateTime requestArrivalTime;
    private Double totalEstimateDistance;
    private Duration totalEstimateDurationTime;
    List<HubListDto> hubList;

    @Getter
    public static class HubListDto {
        UUID hubId;
        String hubName;
        Double distance;
        Double estimateDistance;
        Duration estimateDurationTime;
    }

    @Builder
    public DeliveryManagerRequestDto(
            UUID deliveryId,
            UUID orderId,
            String productInfo,
            String ordererName,
            String startHubName,
            String endHubName,
            UUID currentHubId,
            String currentHubName,
            UUID nextHubId,
            String nextHubName,
            String orderDescription,
            String shippingAddress,
            LocalDateTime requestArrivalTime,
            Double totalEstimateDistance,
            Duration totalEstimateDurationTime,
            List<HubListDto> hubList
    ) {
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.productInfo = productInfo;
        this.ordererName = ordererName;
        this.startHubName = startHubName;
        this.endHubName = endHubName;
        this.currentHubId = currentHubId;
        this.currentHubName = currentHubName;
        this.nextHubId = nextHubId;
        this.nextHubName = nextHubName;
        this.orderDescription = orderDescription;
        this.shippingAddress = shippingAddress;
        this.requestArrivalTime = requestArrivalTime;
        this.totalEstimateDistance = totalEstimateDistance;
        this.totalEstimateDurationTime = totalEstimateDurationTime;
        this.hubList = hubList;
    }

    public static DeliveryManagerRequestDto toDto(Delivery delivery, DeliveryHistory deliveryHistory, List<HubListDto> hubListDtos) {
        return DeliveryManagerRequestDto.builder()
                .deliveryId(delivery.getId())
                .orderId(delivery.getOrderId())
                .productInfo(delivery.getProductInfo())
                .ordererName(delivery.getConsumerName())
                .startHubName(delivery.getStartHubName())
                .endHubName(delivery.getDestinationHubName())
                .currentHubId(deliveryHistory.getStartHubId())
                .currentHubName(deliveryHistory.getStartHubName())
                .nextHubId(deliveryHistory.getDestinationHubId())
                .nextHubName(deliveryHistory.getDestinationHubName())
                .orderDescription(delivery.getDescription())
                .shippingAddress(delivery.getShippingAddress())
                .requestArrivalTime(delivery.getRequestArrivalTime())
                .totalEstimateDistance(delivery.getTotalEstimateDistance())
                .totalEstimateDurationTime(delivery.getTotalEstimateDurationTime())
                .hubList(hubListDtos)
                .build();

    }
}
