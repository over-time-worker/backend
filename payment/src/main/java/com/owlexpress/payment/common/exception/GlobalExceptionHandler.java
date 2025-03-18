package com.owlexpress.payment.common.exception;

import com.owlexpress.payment.presentation.dto.CommonDto;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j(topic = "전역 예외 처리")
public class GlobalExceptionHandler {

    public static final String METHOD_ARGUMENT_NOT_VALID = "MethodArgumentNotValidException = {}";
    private static final String CONSTRAINT_VIOLATION_EXCEPTION = "ConstraintViolationException = {}";


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

}
