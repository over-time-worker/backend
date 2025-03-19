package com.owlexpress.payment.presentation;

import com.owlexpress.payment.application.PaymentUseCase;
import com.owlexpress.payment.common.ResponseMessage;
import com.owlexpress.payment.presentation.dto.CommonDto;
import com.owlexpress.payment.presentation.dto.request.PaymentCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentUseCase paymentUseCase;

    @PostMapping
    public ResponseEntity<CommonDto<Void>> create(
            @ModelAttribute PaymentCreateRequestDto requestDto) {
        paymentUseCase.createPayment(requestDto);

        CommonDto<Void> created = CommonDto.<Void>builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .message(ResponseMessage.PAYMENT_CREATE_SUCESS)
                .data(null)
                .build();

        return ResponseEntity.ok(created);
    }


}
