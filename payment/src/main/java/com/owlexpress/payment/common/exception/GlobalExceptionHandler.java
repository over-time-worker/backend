package com.owlexpress.payment.common.exception;

import com.owlexpress.payment.common.exception.PaymentException.OrderDoesNotMatchException;
import com.owlexpress.payment.common.exception.PaymentException.OrderNotFoundException;
import com.owlexpress.payment.presentation.dto.CommonDto;
import feign.FeignException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "전역 예외 처리")
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    public static final String METHOD_ARGUMENT_NOT_VALID = "MethodArgumentNotValidException = {}";
    private static final String CONSTRAINT_VIOLATION_EXCEPTION = "ConstraintViolationException = {}";
    private static final String ORDER_DOES_NOT_MATCH = "OrderDoesNotMatchException = {}";
    private static final String ORDER_NOT_FOUND = "OrderNotFoundException = {}";
    private static final String FEIGN_EXCEPTION = "FeignException = {}";
    private static final String GENERIC_EXCEPTION = "Exception = {}";

    private final List<FeignExceptionHandlerStrategy> strategies;

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<CommonDto<Void>> handleValidationException(FeignException e) {
        log.error(FEIGN_EXCEPTION, e.getMessage(), e);

        return strategies.stream()
                .filter(strategy -> strategy.supports(e))
                .findFirst()
                .map(strategy -> strategy.handleException(e))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(
                                CommonDto.<Void>builder()
                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                        .message("알 수 없는 Feign 오류 발생: " + e.getMessage())
                                        .data(null)
                                        .build()
                        )
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        log.error(METHOD_ARGUMENT_NOT_VALID, e.getBindingResult().getAllErrors());

        return CommonDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getBindingResult().getAllErrors().toString())
                .data(null)
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleConstraintViolationException(ConstraintViolationException e) {
        log.error(CONSTRAINT_VIOLATION_EXCEPTION, e.getMessage());
        return CommonDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .data(null)
                .build();

    }

    @ExceptionHandler(OrderDoesNotMatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleOrderDoesNotMatchException(OrderDoesNotMatchException e) {
        log.error(ORDER_DOES_NOT_MATCH, e.getMessage());
        return CommonDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .data(null)
                .build();

    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleOrderNotFoundException(OrderNotFoundException e) {
        log.error(ORDER_NOT_FOUND, e.getMessage());
        return CommonDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .data(null)
                .build();

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonDto<Object> handleGenericException(Exception e) {
        log.error(GENERIC_EXCEPTION, e.getMessage(), e);

        return CommonDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("예기치 않은 오류가 발생했습니다.")
                .data(null)
                .build();
    }
}
