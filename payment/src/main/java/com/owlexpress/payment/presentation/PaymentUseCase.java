package com.owlexpress.payment.presentation;

import com.owlexpress.payment.application.dto.response.PaymentFindResponseDto;
import com.owlexpress.payment.presentation.dto.request.PaymentCreateRequestDto;
import com.owlexpress.payment.presentation.dto.request.PaymentDeleteRequestDto;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public interface PaymentUseCase {

    @Transactional
    UUID createPayment(PaymentCreateRequestDto requestDto, String passport);

    @Transactional
    void deletePayment(PaymentDeleteRequestDto requestDto, String passport);

    PaymentFindResponseDto find(UUID orderId);
}
