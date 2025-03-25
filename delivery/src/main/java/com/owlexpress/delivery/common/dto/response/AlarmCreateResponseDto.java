package com.owlexpress.delivery.common.dto.response;

import com.owlexpress.delivery.common.dto.request.DeliveryManagerRequestDto;
import jakarta.validation.constraints.Size;
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
public class AlarmCreateResponseDto {

    private UUID aiId;
    private String platformMessageId;
    private UUID deliverId;
    private Long deliverUserId;
    private String deliverName;
    private String deliverChannelId;
    private String deliverPhoneNumber;
    private String platformName;
    private UUID deliveryId;
    private UUID orderId;
    private String productInfo;
    @Size(min = 1, max = 50, message = "[size:ordererName]")
    private String ordererName;
    @Size(min = 1, max = 50, message = "[size:startHubName]")
    private String startHubName;
    @Size(min = 1, max = 50, message = "[size:endHubName]")
    private String endHubName;
    @Size(min = 1, max = 50, message = "[size:currentHubId]")
    private UUID currentHubId;
    @Size(min = 1, max = 50, message = "[size:currentHubName]")
    private String currentHubName;
    @Size(min = 1, max = 50, message = "[size:nextHubId]")
    private UUID nextHubId;
    @Size(min = 1, max = 50, message = "[size:nextHubName]")
    private String nextHubName;
    private String orderDescription;
    private String shippingAddress;
    private LocalDateTime requestArrivalTime;
    private Double totalEstimateDistance;
    List<DeliveryManagerRequestDto.HubListDto> totalHubList;
    private Duration totalEstimateDurationTime;


    @Builder
    public AlarmCreateResponseDto(
            UUID aiId,
            String platformMessageId,
            UUID deliverId,
            Long deliverUserId,
            String deliverName,
            String deliverChannelId,
            String deliverPhoneNumber,
            String platformName,
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
            List<DeliveryManagerRequestDto.HubListDto> totalHubList,
            Duration totalEstimateDurationTime
    ) {
        this.aiId = aiId;
        this.platformMessageId = platformMessageId;
        this.deliverId = deliverId;
        this.deliverUserId = deliverUserId;
        this.deliverName = deliverName;
        this.deliverChannelId = deliverChannelId;
        this.deliverPhoneNumber = deliverPhoneNumber;
        this.platformName = platformName;
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
        this.totalHubList = totalHubList;
        this.totalEstimateDurationTime = totalEstimateDurationTime;
    }

    @Getter
    public static class HubListDto {

        UUID hubId;
        String hubName;
        Double estimateDistance;
        Duration estimateDurationTime;

    }
}