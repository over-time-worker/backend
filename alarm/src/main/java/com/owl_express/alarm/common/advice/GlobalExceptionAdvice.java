package com.owl_express.alarm.common.advice;

import com.owl_express.alarm.application.dtos.CommonDto;
import com.owl_express.alarm.application.exceptions.AlarmException.AiFeignClientException;
import com.owl_express.alarm.application.exceptions.AlarmException.AlarmNotFoundException;
import com.owl_express.alarm.application.exceptions.AlarmException.NotSupportedMessageTypeException;
import com.owl_express.alarm.application.exceptions.AlarmException.NotSupportedPlatformTypeException;
import com.owl_express.alarm.application.exceptions.AlarmException.OrderNotFoundException;
import com.owl_express.alarm.application.exceptions.AlarmException.SlackException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        log.error("handleMethodArgumentNotValidException : {}", e.getBindingResult().getAllErrors());

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

        log.error("handleConstraintViolationException : {}", e.getMessage());

        return CommonDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .data(null)
                .build();

    }

    @ExceptionHandler(AiFeignClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonDto<Object> handleAiFeignClientException(AiFeignClientException e) {

        log.error("handleAiFeignClientException : {}", e.getMessage());

        return CommonDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .data(null)
                .build();
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonDto<Object> handleOrderNotFoundException(OrderNotFoundException e) {

        log.error("handleOrderNotFoundException : {}", e.getMessage());

        return CommonDto.builder()
                .status(HttpStatus.NOT_FOUND)
                .code(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .data(null)
                .build();
    }

    @ExceptionHandler(AlarmNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonDto<Object> handleAlarmNotFoundException(AlarmNotFoundException e) {

        log.error("handleAlarmNotFoundException : {}", e.getMessage());

        return CommonDto.builder()
                .status(HttpStatus.NOT_FOUND)
                .code(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .data(null)
                .build();
    }

    @ExceptionHandler(NotSupportedPlatformTypeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonDto<Object> handlePlatformTypeNotSupportedException(NotSupportedPlatformTypeException e) {

        log.error("handlePlatformTypeNotSupportedException : {}", e.getMessage());

        return CommonDto.builder()
                .status(HttpStatus.NOT_FOUND)
                .code(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .data(null)
                .build();
    }

    @ExceptionHandler(NotSupportedMessageTypeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonDto<Object> handleMessageTypeNotSupportedException(NotSupportedMessageTypeException e) {

        log.error("handleMessageTypeNotSupportedException : {}", e.getMessage());

        return CommonDto.builder()
                .status(HttpStatus.NOT_FOUND)
                .code(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .data(null)
                .build();
    }

    @ExceptionHandler(SlackException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonDto<Object> handleSlackException(SlackException e) {

        log.error("handleSlackException : {}", e.getMessage());

        return CommonDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .data(null)
                .build();
    }

}
