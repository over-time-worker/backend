package com.owlexpress.auth.common.exception;

import com.owlexpress.auth.application.dto.CommonDto;
import feign.FeignException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "")
@RestControllerAdvice
@RequiredArgsConstructor
public class globalExceptionHandler {

    private static final String FEIGN_EXCEPTION = "FeignException = {}";

    private final List<FeignExceptionHandlerStrategy> strategies;

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<CommonDto<Void>> handleValidationException(FeignException e) {
        log.error(FEIGN_EXCEPTION, e.getMessage(), e);

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
