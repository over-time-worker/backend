package com.owlexpress.payment.presentation.dto.request;

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
}
