package com.owlexpress.payment.application;

import com.owlexpress.payment.common.exception.PaymentException;
import com.owlexpress.payment.common.exception.PaymentException.PaymentNotFoundException;
import com.owlexpress.payment.domain.entity.Payment;
import com.owlexpress.payment.domain.repository.PaymentRepository;
import com.owlexpress.payment.presentation.dto.request.PaymentCreateRequestDto;
import com.owlexpress.payment.presentation.dto.request.PaymentDeleteRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;


@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentUseCase {

    private static final String IDEMPOTENCY_KEY = "Idempotency-Key";

    private final WebClient webClient;
    private final PaymentRepository paymentRepository;

    public void createPayment(PaymentCreateRequestDto requestDto) {
        Payment payment = requestDto.toEntity();
        // TODO : 결제사 승인 요청

        // TODO : PASSPORT에서 값 추출
        payment.createdEntity(1L);
        paymentRepository.save(payment);

    }

    @Transactional
    public void deletePayment(PaymentDeleteRequestDto requestDto) {
        Payment payment = paymentRepository.findByTransactionId(requestDto.getTransactionId())
                .orElseThrow(PaymentNotFoundException::new);

        // TODO: 결제사 취소 요청

        // TODO: PASSPORT에서 값 추출
        payment.deleteEntity(1L);
    }
}
