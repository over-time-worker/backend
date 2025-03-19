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
    private BigDecimal amount;
    private String paymentKey;

    @Builder
    public PaymentCreateRequestDto(UUID orderId, BigDecimal amount, String paymentKey) {
        this.orderId = orderId;
        this.amount = amount;
        this.paymentKey = paymentKey;
    }

    public Payment toEntity() {
        return Payment.builder()
                .paymentStatus(PaymentStatus.SUCCESS)
                .transactionId(this.paymentKey)
                .orderId(orderId)
                .price(amount)
                .build();
    }
}
