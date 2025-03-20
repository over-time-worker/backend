package com.owlexpress.deliverymanager.presentation.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
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
    List<HubListDto> totalHubList;

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
            List<HubListDto> totalHubList
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
        this.totalHubList = totalHubList;
    }

    @Getter
    public static class HubListDto {
        UUID hubId;
        String hubName;
        Double estimateDistance;
        Duration estimateDurationTime;
    }
}
