package com.owl_express.alarm.common.advice;

import com.owl_express.alarm.application.dtos.CommonDto;
import com.owl_express.alarm.application.exceptions.AlarmException.AiFeignClientException;
import com.owl_express.alarm.application.exceptions.AlarmException.AlarmNotFoundException;
import com.owl_express.alarm.application.exceptions.AlarmException.NotSupportedMessageTypeException;
import com.owl_express.alarm.application.exceptions.AlarmException.NotSupportedPlatformTypeException;
import com.owl_express.alarm.application.exceptions.AlarmException.SlackException;
import com.owl_express.alarm.application.exceptions.FeignExceptionHandlerStrategy;
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

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionAdvice {

    public static final String METHOD_ARGUMENT_NOT_VALID_EXCEPTION = "handleMethodArgumentNotValidException : {}";
    public static final String CONSTRAIN_VIOLATION_EXCEPTION = "handleConstraintViolationException : {}";
    public static final String AI_FEIGN_CLIENT_EXCEPTION = "handleAiFeignClientException : {}";
    public static final String ALARM_NOT_FOUND_EXCEPTION = "handleAlarmNotFoundException : {}";
    public static final String PLATFORM_TYPE_NOT_SUPPORTED_EXCEPTION = "handlePlatformTypeNotSupportedException : {}";
    public static final String MESSAGE_TYPE_NOT_SUPPORTED_EXCEPTION = "handleMessageTypeNotSupportedException : {}";
    public static final String SLACK_EXCEPTION = "handleSlackException : {}";
    public static final String FEIGN_EXCEPTION = "handleFeignException : {}";

    public final List<FeignExceptionHandlerStrategy> strategies;

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

    @ExceptionHandler(AiFeignClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonDto<Object> handleAiFeignClientException(AiFeignClientException e) {

        log.error(AI_FEIGN_CLIENT_EXCEPTION, e.getMessage());

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
    public CommonDto<Object> handlePlatformTypeNotSupportedException(NotSupportedPlatformTypeException e) {

        log.error(PLATFORM_TYPE_NOT_SUPPORTED_EXCEPTION, e.getMessage());

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

        log.error(MESSAGE_TYPE_NOT_SUPPORTED_EXCEPTION, e.getMessage());

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

        log.error(SLACK_EXCEPTION, e.getMessage());

        return CommonDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .data(null)
                .build();
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<CommonDto<Void>> handleFeignException(FeignException e) {
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
