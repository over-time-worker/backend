package com.owlexpress.payment.presentation.dto.request;

import com.owlexpress.payment.domain.constant.PaymentStatus;
import com.owlexpress.payment.domain.entity.Payment;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentCreateRequestDto {

    private UUID orderId;
    private BigDecimal price;
    private String transactionId;

    @Builder
    public PaymentCreateRequestDto(UUID orderId, String paymentKey, BigDecimal price) {
        this.orderId = orderId;
        this.price = price;
        this.transactionId = paymentKey;
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
