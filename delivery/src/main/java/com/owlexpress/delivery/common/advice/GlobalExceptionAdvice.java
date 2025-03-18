package com.owlexpress.delivery.common.advice;

import com.owlexpress.delivery.application.dtos.CommonDto;
import com.owlexpress.delivery.application.exceptions.DeliveryException.AlarmFeignClientException;
import com.owlexpress.delivery.application.exceptions.DeliveryException.AlarmNotFoundException;
import com.owlexpress.delivery.application.exceptions.DeliveryException.NotSupportedPlatformTypeException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    public static final String METHOD_ARGUMENT_NOT_VALID_EXCEPTION = "handleMethodArgumentNotValidException : {}";
    public static final String CONSTRAIN_VIOLATION_EXCEPTION = "handleConstraintViolationException : {}";
    public static final String ALARM_FEIGN_CLIENT_EXCEPTION = "handleAlarmFeignClientException : {}";
    public static final String ALARM_NOT_FOUND_EXCEPTION = "handleAlarmNotFoundException : {}";
    public static final String PLATFORM_TYPE_NOT_SUPPORTED_EXCEPTION = "handlePlatformTypeNotSupportedException : {}";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        log.error(METHOD_ARGUMENT_NOT_VALID_EXCEPTION, e.getBindingResult().getAllErrors());

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

        log.error(CONSTRAIN_VIOLATION_EXCEPTION, e.getMessage());

        return CommonDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .data(null)
                .build();

    }

    @ExceptionHandler(AlarmFeignClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonDto<Object> handleAlarmFeignClientException(AlarmFeignClientException e) {

        log.error(ALARM_FEIGN_CLIENT_EXCEPTION, e.getMessage());

        return CommonDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .data(null)
                .build();
    }

    @ExceptionHandler(AlarmNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonDto<Object> handleAlarmNotFoundException(AlarmNotFoundException e) {

        log.error(ALARM_NOT_FOUND_EXCEPTION, e.getMessage());

        return CommonDto.builder()
                .status(HttpStatus.NOT_FOUND)
                .code(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .data(null)
                .build();
    }

    @ExceptionHandler(NotSupportedPlatformTypeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonDto<Object> handlePlatformTypeNotSupportedException(
            NotSupportedPlatformTypeException e) {

        log.error(PLATFORM_TYPE_NOT_SUPPORTED_EXCEPTION, e.getMessage());

        return CommonDto.builder()
                .status(HttpStatus.NOT_FOUND)
                .code(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .data(null)
                .build();
    }


}
