package com.owlexpress.deliverymanager.common.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.owlexpress.deliverymanager.domain.entity.ConsumerDeliveryManager;
import com.owlexpress.deliverymanager.domain.entity.HubDeliveryManager;
import com.owlexpress.deliverymanager.presentation.dto.request.DeliveryManagerRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmCreateRequestDto {
    @NotNull(message = "[notNull:deliverId]")
    private UUID deliverId;
    @NotNull(message = "[notNull:userId]")
    private Long deliverUserId;
    @Size(min = 1, max = 50, message = "[size:deliverName]")
    private String deliverName;
    @Size(min = 1, max = 50, message = "[size:deliverPlatformId]")
    private String deliverChannelId;
    @Size(min = 1, max = 50, message = "[size:platformName]")
    private String platformName;
    @NotNull(message = "[notNull:deliveryId]")
    private UUID deliveryId;
    @NotNull(message = "[notNull:orderId]")
    private UUID orderId;
    @NotBlank(message = "[notBlank:productInfo]")
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
    @NotNull(message = "[notNull:shippingAddress]")
    private String shippingAddress;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime requestArrivalTime;
    @NotNull(message = "[notNull:totalEstimateDistance]")
    private Double totalEstimateDistance;
    @NotNull(message = "[notNull:totalEstimateDurationTime]")
    private Duration totalEstimateDurationTime;
    @NotNull(message = "[notNull:totalHubList]")
    List<HubListDto> totalHubList;

    @Getter
    public static class HubListDto {
        UUID hubId;
        String hubName;
        Double estimateDistance;
        Duration estimateDurationTime;

        @Builder
        public HubListDto(
                UUID hubId,
                String hubName,
                Double estimateDistance,
                Duration estimateDurationTime
        ) {
            this.hubId = hubId;
            this.hubName = hubName;
            this.estimateDistance = estimateDistance;
            this.estimateDurationTime = estimateDurationTime;
        }
    }

    @Builder
    public AlarmCreateRequestDto(
            UUID deliverId,
            Long deliverUserId,
            String deliverName,
            String deliverChannelId,
            String platformName,
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
        this.deliverId = deliverId;
        this.deliverUserId = deliverUserId;
        this.deliverName = deliverName;
        this.deliverChannelId = deliverChannelId;
        this.platformName = platformName;
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

    public static AlarmCreateRequestDto toDto(
            DeliveryManagerRequestDto deliveryManagerRequestDto,
            HubDeliveryManager manager
    ) {
        List<HubListDto> hubListDtos = deliveryManagerRequestDto.getTotalHubList()
                                                                .stream()
                                                                .map(hubListDto -> HubListDto.builder()
                                                                                             .hubId(hubListDto.getHubId())
                                                                                             .hubName(
                                                                                                     hubListDto.getHubName())
                                                                                             .estimateDistance(
                                                                                                     hubListDto.getEstimateDistance())
                                                                                             .estimateDurationTime(
                                                                                                     hubListDto.getEstimateDurationTime())
                                                                                             .build())
                                                                .toList();
        return AlarmCreateRequestDto.builder()
                             .deliverId(deliveryManagerRequestDto.getDeliveryId())
                             .deliverUserId(manager.getUserId())
                             .deliverName(manager.getUserName())
                             .deliverChannelId(manager.getChannelId())
                             .platformName(String.valueOf(manager.getPlatformType()))
                             .orderId(deliveryManagerRequestDto.getOrderId())
                             .productInfo(deliveryManagerRequestDto.getProductInfo())
                             .ordererName(deliveryManagerRequestDto.getOrdererName())
                             .startHubName(deliveryManagerRequestDto.getStartHubName())
                             .endHubName(deliveryManagerRequestDto.getEndHubName())
                             .currentHubId(deliveryManagerRequestDto.getCurrentHubId())
                             .currentHubName(deliveryManagerRequestDto.getCurrentHubName())
                             .nextHubId(deliveryManagerRequestDto.getNextHubId())
                             .nextHubName(deliveryManagerRequestDto.getNextHubName())
                             .orderDescription(deliveryManagerRequestDto.getOrderDescription())
                             .shippingAddress(deliveryManagerRequestDto.getShippingAddress())
                             .requestArrivalTime(deliveryManagerRequestDto.getRequestArrivalTime())
                             .totalEstimateDistance(deliveryManagerRequestDto.getTotalEstimateDistance())
                             .totalEstimateDurationTime(deliveryManagerRequestDto.getTotalEstimateDurationTime())
                             .totalHubList(hubListDtos)
                             .build();

    }


    public static AlarmCreateRequestDto toDto(
            DeliveryManagerRequestDto deliveryManagerRequestDto,
            ConsumerDeliveryManager manager
    ) {
        List<HubListDto> hubListDtos = deliveryManagerRequestDto.getTotalHubList()
                                                                .stream()
                                                                .map(hubListDto -> HubListDto.builder()
                                                                                             .hubId(hubListDto.getHubId())
                                                                                             .hubName(
                                                                                                     hubListDto.getHubName())
                                                                                             .estimateDistance(
                                                                                                     hubListDto.getEstimateDistance())
                                                                                             .estimateDurationTime(
                                                                                                     hubListDto.getEstimateDurationTime())
                                                                                             .build())
                                                                .toList();
        return AlarmCreateRequestDto.builder()
                             .deliverId(deliveryManagerRequestDto.getDeliveryId())
                             .deliverUserId(manager.getUserId())
                             .deliverName(manager.getUserName())
                             .deliverChannelId(String.valueOf(manager.getChannelId()))
                             .platformName(String.valueOf(manager.getPlatformType()))
                             .orderId(deliveryManagerRequestDto.getOrderId())
                             .productInfo(deliveryManagerRequestDto.getProductInfo())
                             .ordererName(deliveryManagerRequestDto.getOrdererName())
                             .startHubName(deliveryManagerRequestDto.getStartHubName())
                             .endHubName(deliveryManagerRequestDto.getEndHubName())
                             .currentHubId(deliveryManagerRequestDto.getCurrentHubId())
                             .currentHubName(deliveryManagerRequestDto.getCurrentHubName())
                             .nextHubId(deliveryManagerRequestDto.getNextHubId())
                             .nextHubName(deliveryManagerRequestDto.getNextHubName())
                             .orderDescription(deliveryManagerRequestDto.getOrderDescription())
                             .shippingAddress(deliveryManagerRequestDto.getShippingAddress())
                             .requestArrivalTime(deliveryManagerRequestDto.getRequestArrivalTime())
                             .totalEstimateDistance(deliveryManagerRequestDto.getTotalEstimateDistance())
                             .totalEstimateDurationTime(deliveryManagerRequestDto.getTotalEstimateDurationTime())
                             .totalHubList(hubListDtos)
                             .build();

    }
}
