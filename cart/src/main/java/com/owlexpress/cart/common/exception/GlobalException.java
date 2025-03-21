package com.owlexpress.cart.common.exception;

import com.owlexpress.cart.common.CommonDto;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalException {
    private static final String LOG_WARN_MESSAGE = "Warning exception : {}";
    private final List<FeignExceptionHandlerStrategy> strategies;

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleUserNotFoundException(EntityNotFoundException e) {
        log.error("error ={}", e.getMessage(), e);
        return CommonDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage()) // Ensure the message is included
                .data(null)
                .build();
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<CommonDto<Void>> handleValidationException(FeignException e) {
        log.error("error ={}", e.getMessage(), e);

        return strategies.stream()
                .filter(strategy -> strategy.supports(e))
                .findFirst()
                .map(strategy -> strategy.handleException(e))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        CommonDto.<Void>builder()
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .message("알 수 없는 Feign 오류 발생: " + e.getMessage())
                                .data(null)
                                .build()
                ));
    }
}
