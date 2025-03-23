package com.owlexpress.payment.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.owlexpress.payment.common.OrderType;
import com.owlexpress.payment.domain.constant.PaymentStatus;
import com.owlexpress.payment.domain.entity.Payment;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentCreateRequestDto {

    private UUID orderId;
    private BigDecimal price;
    private String transactionId;
    private String productInfo;
    private UUID startHubId;
    private String startHubName;
    private OrderType orderType;
    private String shippingAddress;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime requestArrivalTime;
    private UUID consumerCompanyId;
    private Double consumerLatitude;
    private Double consumerLongitude;
    private String consumerPhoneNumber;
    private String consumerName;


    public PaymentCreateRequestDto(
            UUID orderId,
            BigDecimal price,
            String transactionId,
            String productInfo,
            UUID startHubId,
            String startHubName,
            OrderType orderType,
            String shippingAddress,
            String description,
            LocalDateTime requestArrivalTime,
            UUID consumerCompanyId,
            Double consumerLatitude,
            Double consumerLongitude,
            String consumerPhoneNumber,
            String consumerName
    ) {
        this.orderId = orderId;
        this.price = price;
        this.transactionId = transactionId;
        this.productInfo = productInfo;
        this.startHubId = startHubId;
        this.startHubName = startHubName;
        this.orderType = orderType;
        this.shippingAddress = shippingAddress;
        this.description = description;
        this.requestArrivalTime = requestArrivalTime;
        this.consumerCompanyId = consumerCompanyId;
        this.consumerLatitude = consumerLatitude;
        this.consumerLongitude = consumerLongitude;
        this.consumerPhoneNumber = consumerPhoneNumber;
        this.consumerName = consumerName;
    }

    public Payment toEntity() {
        return Payment.builder()
                .orderId(this.orderId)
                .price(this.price)
                .transactionId(this.transactionId)
                .paymentStatus(PaymentStatus.SUCCESS)
                .build();
    }
}
