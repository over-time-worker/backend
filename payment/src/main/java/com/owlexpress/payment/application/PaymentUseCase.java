package com.owlexpress.payment.application;

import com.owlexpress.payment.application.dto.response.OrderFindResponseDto;
import com.owlexpress.payment.application.dto.response.PaymentFindResponseDto;
import com.owlexpress.payment.common.exception.PaymentException;
import com.owlexpress.payment.common.exception.PaymentException.OrderDoesNotMatchException;
import com.owlexpress.payment.common.exception.PaymentException.OrderNotFoundException;
import com.owlexpress.payment.common.exception.PaymentException.PaymentNotFoundException;
import com.owlexpress.payment.domain.entity.Payment;
import com.owlexpress.payment.domain.repository.PaymentRepository;
import com.owlexpress.payment.infrastructure.client.OrderClient;
import com.owlexpress.payment.presentation.dto.CommonDto;
import com.owlexpress.payment.presentation.dto.request.PaymentCreateRequestDto;
import com.owlexpress.payment.presentation.dto.request.PaymentDeleteRequestDto;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
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
    private final OrderClient orderClient;

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

    public PaymentFindResponseDto find(UUID orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(OrderDoesNotMatchException::new);

        // TODO : orderClient 연결
        // OrderFindResponseDto data = orderClient.findOrderDetails(orderId).getData();
        OrderFindResponseDto data = OrderFindResponseDto.builder()
                .totalPrice(new BigDecimal(1000))
                .products(Collections.emptyList())
                .build();

        // 에러 발생 시
//        if (data == null) {
//            throw new OrderNotFoundException();
//        }
        PaymentFindResponseDto paymentFindResponseDto = data.toPaymentFindResponseDto();
        paymentFindResponseDto.setInfo(payment);

        return paymentFindResponseDto;

    }
}
