package com.owlexpress.delivery.application.dtos;

import com.owlexpress.delivery.domain.entity.Delivery;
import com.owlexpress.delivery.domain.entity.Delivery.DeliveryStatus;
import com.owlexpress.delivery.domain.entity.Delivery.OrderType;
import com.owlexpress.delivery.domain.entity.DeliveryHistory;
import com.owlexpress.delivery.domain.entity.DeliveryHistory.PlatformType;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryCacheDto {
    private UUID id;
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
    private List<DeliveryHistoryCacheDto> deliveryHistories;

    @Builder
    public DeliveryCacheDto(
            UUID id,
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
            List<DeliveryHistoryCacheDto> deliveryHistories
    ) {
        this.id = id;
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

    public static DeliveryCacheDto toCacheDto(Delivery delivery) {
        return DeliveryCacheDto.builder()
                .id(delivery.getId())
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
                .deliveryHistories(
                        delivery.getDeliveryHistories().stream()
                                .map(DeliveryHistoryCacheDto::toCacheDto)
                                .collect(Collectors.toList())
                        )
                .build();
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class DeliveryHistoryCacheDto {
        private UUID id;
        private Delivery delivery;
        private Integer sequence;
        private UUID startHubId;
        private String startHubName;
        private UUID destinationHubId;
        private String destinationHubName;
        private String shippingAddress;
        private Double estimateDistance;
        private Duration estimateDurationTime;
        private Double actualDistance;
        private Duration actualTime;
        private DeliveryStatus deliveryStatus;
        private UUID deliverId;
        private String deliverPhoneNumber;
        private String deliverName;
        private PlatformType platformType;
        private String deliverChannelId;

        @Builder
        public DeliveryHistoryCacheDto(
                UUID id,
                Integer sequence,
                UUID startHubId,
                String startHubName,
                UUID destinationHubId,
                String destinationHubName,
                String shippingAddress,
                Double estimateDistance,
                Duration estimateDurationTime,
                Double actualDistance,
                Duration actualTime,
                DeliveryStatus deliveryStatus,
                UUID deliverId,
                String deliverPhoneNumber,
                String deliverName,
                PlatformType platformType,
                String deliverChannelId
        ) {
            this.id = id;
            this.sequence = sequence;
            this.startHubId = startHubId;
            this.startHubName = startHubName;
            this.destinationHubId = destinationHubId;
            this.destinationHubName = destinationHubName;
            this.shippingAddress = shippingAddress;
            this.estimateDistance = estimateDistance;
            this.estimateDurationTime = estimateDurationTime;
            this.actualDistance = actualDistance;
            this.actualTime = actualTime;
            this.deliveryStatus = deliveryStatus;
            this.deliverId = deliverId;
            this.deliverPhoneNumber = deliverPhoneNumber;
            this.deliverName = deliverName;
            this.platformType = platformType;
            this.deliverChannelId = deliverChannelId;
        }

        public static DeliveryHistoryCacheDto toCacheDto(DeliveryHistory deliveryHistory) {
            return DeliveryHistoryCacheDto.builder()
                    .id(deliveryHistory.getId())
                    .sequence(deliveryHistory.getSequence())
                    .startHubId(deliveryHistory.getStartHubId())
                    .startHubName(deliveryHistory.getStartHubName())
                    .destinationHubId(deliveryHistory.getDestinationHubId())
                    .destinationHubName(deliveryHistory.getDestinationHubName())
                    .shippingAddress(deliveryHistory.getShippingAddress())
                    .estimateDistance(deliveryHistory.getEstimateDistance())
                    .estimateDurationTime(deliveryHistory.getEstimateDurationTime())
                    .actualDistance(deliveryHistory.getActualDistance())
                    .actualTime(deliveryHistory.getActualTime())
                    .deliveryStatus(deliveryHistory.getDeliveryStatus())
                    .deliverId(deliveryHistory.getDeliverId())
                    .deliverPhoneNumber(deliveryHistory.getDeliverPhoneNumber())
                    .deliverName(deliveryHistory.getDeliverName())
                    .platformType(deliveryHistory.getPlatformType())
                    .deliverChannelId(deliveryHistory.getDeliverChannelId())
                    .build();
        }
    }
}
