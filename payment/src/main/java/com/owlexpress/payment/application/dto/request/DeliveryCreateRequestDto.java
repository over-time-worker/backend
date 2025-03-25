package com.owlexpress.payment.application.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.owlexpress.payment.application.dto.response.RouteInfoResponseDto;
import com.owlexpress.payment.common.constant.OrderType;
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
public class DeliveryCreateRequestDto {

    private UUID orderId;
    private String productInfo;
    private UUID startHubId;
    private String startHubName;
    private UUID destinationHubId;
    private String destinationHubName;
    private OrderType orderType;
    private String shippingAddress;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime requestArrivalTime;
    private UUID consumerCompanyId;
    private String consumerPhoneNumber;
    private String consumerName;
    private Double totalEstimateDistance;
    private Duration totalEstimateDurationTime;
    private List<RouteInfoResponseDto> hubList;


    @Builder
    public DeliveryCreateRequestDto(
            UUID orderId,
            String productInfo,
            UUID startHubId,
            String startHubName,
            UUID destinationHubId,
            String destinationHubName,
            OrderType orderType,
            String shippingAddress,
            String description,
            LocalDateTime requestArrivalTime,
            UUID consumerCompanyId,
            String consumerPhoneNumber,
            String consumerName,
            Double totalEstimateDistance,
            Duration totalEstimateDurationTime,
            List<RouteInfoResponseDto> hubList
    ) {
        this.orderId = orderId;
        this.productInfo = productInfo;
        this.startHubId = startHubId;
        this.startHubName = startHubName;
        this.destinationHubId = destinationHubId;
        this.destinationHubName = destinationHubName;
        this.orderType = orderType;
        this.shippingAddress = shippingAddress;
        this.description = description;
        this.requestArrivalTime = requestArrivalTime;
        this.consumerCompanyId = consumerCompanyId;
        this.consumerPhoneNumber = consumerPhoneNumber;
        this.consumerName = consumerName;
        this.totalEstimateDistance = totalEstimateDistance;
        this.totalEstimateDurationTime = totalEstimateDurationTime;
        this.hubList = hubList;
    }
}