package com.owlexpress.deliverymanager.application.dto.response;

import com.owlexpress.deliverymanager.domain.entity.ConsumerDeliveryManager;
import com.owlexpress.deliverymanager.domain.entity.HubDeliveryManager;
import com.owlexpress.deliverymanager.presentation.dto.request.DeliveryManagerRequestDto;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class AlarmCreateResponseDto {
    private UUID aiId;
    private String platformMessageId;
    private UUID deliverId;
    private Long deliverUserId;
    private String deliverName;
    private String deliverChannelId;
    private String platformName;
    private String deliverPhoneNumber;
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
        this.deliverPhoneNumber = deliverPhoneNumber;
    }

    @Getter
    public static class HubListDto {

        UUID hubId;
        String hubName;
        Double estimateDistance;
        Duration estimateDurationTime;

    }

    public static AlarmCreateResponseDto from(
            ConsumerDeliveryManager manager,
            DeliveryManagerRequestDto request
    ) {
        // TODO : alarm과 feign 연결된 이후에 alarm에서 response 해준 값으로 aiId와 platformMessageId를 추가해주어야합니다..!
        return AlarmCreateResponseDto.builder()
                                     .deliverId(manager.getConsumerDeliveryManagerId())
                                     .deliverUserId(manager.getUserId())
                                     .deliverName(manager.getUserName())
                                     .deliverChannelId(manager.getChannelId()
                                                              .toString()) // Long -> String 변환 필요
                                     .platformName(manager.getPlatformType()
                                                          .name()) // Enum -> String 변환
                                     .deliveryId(request.getDeliveryId())
                                     .deliverPhoneNumber(manager.getUserPhoneNumber())
                                     .orderId(request.getOrderId())
                                     .productInfo(request.getProductInfo())
                                     .ordererName(request.getOrdererName())
                                     .startHubName(request.getStartHubName())
                                     .endHubName(request.getEndHubName())
                                     .currentHubId(request.getCurrentHubId())
                                     .currentHubName(request.getCurrentHubName())
                                     .nextHubId(request.getNextHubId())
                                     .nextHubName(request.getNextHubName())
                                     .orderDescription(request.getOrderDescription())
                                     .shippingAddress(request.getShippingAddress())
                                     .requestArrivalTime(request.getRequestArrivalTime())
                                     .totalEstimateDistance(request.getTotalEstimateDistance())
                                     .totalEstimateDurationTime(request.getTotalEstimateDurationTime())
                                     .totalHubList(request.getTotalHubList())
                                     .build();
    }

    public static AlarmCreateResponseDto from(
            HubDeliveryManager manager,
            DeliveryManagerRequestDto request
    ) {
        return AlarmCreateResponseDto.builder()
                                     .deliverId(manager.getHubDeliveryManagerId())
                                     .deliverUserId(manager.getUserId())
                                     .deliverName(manager.getUserName())
                                     .deliverChannelId(manager.getChannelId()
                                                              .toString()) // Long -> String 변환 필요
                                     .platformName(manager.getPlatformType()
                                                          .name()) // Enum -> String 변환
                                     .deliveryId(request.getDeliveryId())
                                     .orderId(request.getOrderId())
                                     .productInfo(request.getProductInfo())
                                     .ordererName(request.getOrdererName())
                                     .startHubName(request.getStartHubName())
                                     .endHubName(request.getEndHubName())
                                     .currentHubId(request.getCurrentHubId())
                                     .currentHubName(request.getCurrentHubName())
                                     .nextHubId(request.getNextHubId())
                                     .nextHubName(request.getNextHubName())
                                     .orderDescription(request.getOrderDescription())
                                     .shippingAddress(request.getShippingAddress())
                                     .requestArrivalTime(request.getRequestArrivalTime())
                                     .totalEstimateDistance(request.getTotalEstimateDistance())
                                     .totalEstimateDurationTime(request.getTotalEstimateDurationTime())
                                     .totalHubList(request.getTotalHubList())
                                     .build();
    }
}