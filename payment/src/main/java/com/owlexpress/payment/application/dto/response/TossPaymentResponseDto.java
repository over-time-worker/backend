package com.owlexpress.payment.application.dto.response;

import com.owlexpress.payment.domain.entity.Payment;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TossPaymentResponseDto {

    private String paymentKey;
    private String orderId;
    private String status;
    private BigDecimal amount;
    private String method;

    @Builder
    public TossPaymentResponseDto(
            String paymentKey,
            String orderId,
            String status,
            String amount,
            String method
    ) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.status = status;
        this.amount = new BigDecimal(amount);
        this.method = method;
    }

    public Payment toEntity() {
        return Payment.builder()
                .orderId(UUID.fromString(this.orderId))
                .transactionId(paymentKey)
                .build();
    }
}