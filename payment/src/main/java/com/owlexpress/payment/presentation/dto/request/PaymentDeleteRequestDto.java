package com.owlexpress.payment.presentation.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentDeleteRequestDto {
    private String transactionId;

    public PaymentDeleteRequestDto(String transactionId) {
        this.transactionId = transactionId;
    }
}
