package com.owlexpress.payment.presentation;

import com.owlexpress.payment.application.PaymentUseCase;
import com.owlexpress.payment.application.dto.response.PaymentFindResponseDto;
import com.owlexpress.payment.common.ResponseMessage;
import com.owlexpress.payment.presentation.dto.CommonDto;
import com.owlexpress.payment.presentation.dto.CommonDto.CommonDtoBuilder;
import com.owlexpress.payment.presentation.dto.request.PaymentCreateRequestDto;
import com.owlexpress.payment.presentation.dto.request.PaymentDeleteRequestDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentUseCase paymentUseCase;

    @PostMapping
    public ResponseEntity<CommonDto<Void>> create(
            @RequestBody PaymentCreateRequestDto requestDto,
            @RequestHeader("X-User-Passport") String passport
    ) {
        paymentUseCase.createPayment(requestDto, passport);

        CommonDto<Void> created = CommonDto.<Void>builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .message(ResponseMessage.PAYMENT_CREATE_SUCESS)
                .data(null)
                .build();

        return ResponseEntity.ok(created);
    }

    @DeleteMapping
    public ResponseEntity<CommonDto<Void>> delete(
            @RequestBody PaymentDeleteRequestDto requestDto,
            @RequestHeader("X-User-Passport") String passport
    ) {
        paymentUseCase.deletePayment(requestDto, passport);

        CommonDto<Void> deleted = CommonDto.<Void>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message(ResponseMessage.PAYMENT_CANCEL_SUCESS)
                .data(null)
                .build();

        return ResponseEntity.ok(deleted);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<CommonDto<PaymentFindResponseDto>> find(
            @PathVariable("orderId") UUID orderId
    ) {
        PaymentFindResponseDto paymentFindResponseDto = paymentUseCase.find(orderId);

        CommonDtoBuilder<PaymentFindResponseDto> found =
                CommonDto.<PaymentFindResponseDto>builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .message(ResponseMessage.PAYMENT_DETAILS_SEARCH_SUCCESS)
                        .data(paymentFindResponseDto);

        return ResponseEntity.ok(found.build());
    }

}
