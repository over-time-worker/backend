package com.owlexpress.payment.application;

import com.owlexpress.payment.application.dto.response.TossPaymentResponseDto;
import com.owlexpress.payment.application.property.TossPayProperty;
import com.owlexpress.payment.domain.constant.PaymentStatus;
import com.owlexpress.payment.domain.entity.Payment;
import com.owlexpress.payment.domain.repository.PaymentRepository;
import com.owlexpress.payment.presentation.dto.request.PaymentCreateRequestDto;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.reactive.function.client.WebClient;


@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentUseCase {

    private static final String IDEMPOTENCY_KEY = "Idempotency-Key";
    private final TossPayProperty tossPayProperty;

    private final WebClient webClient;
    private final PaymentRepository paymentRepository;

    // Base64 인코딩된 인증 헤더 생성
    private String getAuthorizationHeader() {
        byte[] encodedAuth = Base64.getEncoder()
                .encode((tossPayProperty.getSecretKey() + ":").getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedAuth);
    }


    public void createPayment(@ModelAttribute PaymentCreateRequestDto requestDto) {

        // 결제 연동
        TossPaymentResponseDto block = webClient.post()
                .uri("https://api.tosspayments.com/v1/payments/confirm")
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(TossPaymentResponseDto.class)
                .block();

        log.info("{}", block);

        Payment entity = block.toEntity();
        PaymentStatus status = PaymentStatus.FAIL;
        if (block.getStatus().equals("DONE")) {
            status = PaymentStatus.SUCCESS;
        }

        entity.updateWithStatusAndAmount(status, requestDto.getAmount());
        // TODO : PASSPORT에서 값 추출
        entity.createdEntity(1L);
        paymentRepository.save(entity);

    }
}
