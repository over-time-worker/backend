package com.owlexpress.deliverymanager.common.exception;

import com.owlexpress.deliverymanager.common.exception.ConsumerDeliveryManagerException.ConsumerDeliveryManagerNameDuplicateException;
import com.owlexpress.deliverymanager.common.exception.ConsumerDeliveryManagerException.ConsumerDeliveryManagerNotAvailableException;
import com.owlexpress.deliverymanager.common.exception.ConsumerDeliveryManagerException.ConsumerDeliveryManagerNotFoundException;
import com.owlexpress.deliverymanager.common.exception.ConsumerDeliveryManagerException.ConsumerDuplicateAssignNumberException;
import com.owlexpress.deliverymanager.common.exception.HubDeliveryManagerException.HubDeliveryManagerNameDuplicateException;
import com.owlexpress.deliverymanager.common.exception.HubDeliveryManagerException.HubDeliveryManagerNotFoundException;
import com.owlexpress.deliverymanager.common.exception.HubDeliveryManagerException.HubDuplicateAssignNumber;
import com.owlexpress.deliverymanager.common.exception.HubDeliveryManagerException.HubIsNotAvailableStatusException;
import com.owlexpress.deliverymanager.infrastructure.CommonDto;
import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@ControllerAdvice
@RestController
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionAdvice {

    public static final String PRODUCER_NOT_FOUND_EXCEPTION = "handleProducerNotFoundException : {}";
    public static final String PRODUCER_NAME_DUPLICATE_EXCEPTION = "handleProducerNameDuplicateException : {}";
    public static final String FEIGN_EXCEPTION = "handleFeignException : {}";
    public static final String METHOD_ARGUMENT_NOT_VALID_EXCEPTION = "handleMethodArgumentNotValidException : {}";
    public static final String CONSTRAINT_VIOLATION_EXCEPTION = "handleConstraintViolationException : {}";
    public static final String FORBIDDEN_EXCEPTION = "handleForbiddenException : {}";
    public static final String DATABASE_EXCEPTION = "handleDatabaseException : {}";
    public static final String GENERIC_EXCEPTION = "handleGenericException : {}";

    private final List<FeignExceptionHandlerStrategy> strategies;

    @ExceptionHandler(HubDeliveryManagerNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleProducerNotFoundException(HubDeliveryManagerNotFoundException e) {
        log.error(PRODUCER_NOT_FOUND_EXCEPTION, e.getMessage(), e);
        return CommonDto.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage()) // Ensure the message is included
                        .data(null)
                        .build();
    }


    @ExceptionHandler(HubDeliveryManagerNameDuplicateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleProducerNameDuplicateException(HubDeliveryManagerNameDuplicateException e) {
        log.error(PRODUCER_NAME_DUPLICATE_EXCEPTION, e.getMessage(), e);

        return CommonDto.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage()) // Ensure the message is included
                        .data(null)
                        .build();
    }

    @ExceptionHandler(ConsumerDeliveryManagerNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleProducerNotFoundException(ConsumerDeliveryManagerNotFoundException e) {
        log.error(PRODUCER_NOT_FOUND_EXCEPTION, e.getMessage(), e);
        return CommonDto.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage()) // Ensure the message is included
                        .data(null)
                        .build();
    }


    @ExceptionHandler(ConsumerDuplicateAssignNumberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleDuplicateAssignNumberException(ConsumerDuplicateAssignNumberException e) {
        log.error(PRODUCER_NAME_DUPLICATE_EXCEPTION, e.getMessage(), e);

        return CommonDto.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage()) // Ensure the message is included
                        .data(null)
                        .build();
    }

    @ExceptionHandler(HubDuplicateAssignNumber.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleDuplicateAssignNumberException(HubDuplicateAssignNumber e) {
        log.error(PRODUCER_NAME_DUPLICATE_EXCEPTION, e.getMessage(), e);

        return CommonDto.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage()) // Ensure the message is included
                        .data(null)
                        .build();
    }

    @ExceptionHandler(HubIsNotAvailableStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleIsNotAvailableStatusException(HubIsNotAvailableStatusException e) {
        log.error(PRODUCER_NAME_DUPLICATE_EXCEPTION, e.getMessage(), e);

        return CommonDto.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage()) // Ensure the message is included
                        .data(null)
                        .build();
    }


    @ExceptionHandler(ConsumerDeliveryManagerNameDuplicateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleProducerNameDuplicateException(ConsumerDeliveryManagerNameDuplicateException e) {
        log.error(PRODUCER_NAME_DUPLICATE_EXCEPTION, e.getMessage(), e);

        return CommonDto.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage()) // Ensure the message is included
                        .data(null)
                        .build();
    }

    @ExceptionHandler(ConsumerDeliveryManagerNotAvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleNotAvailableException(ConsumerDeliveryManagerNotAvailableException e) {
        log.error(PRODUCER_NAME_DUPLICATE_EXCEPTION, e.getMessage(), e);

        return CommonDto.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(e.getMessage()) // Ensure the message is included
                        .data(null)
                        .build();
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<CommonDto<Void>> handleValidationException(FeignException e) {
        log.error(FEIGN_EXCEPTION, e.getMessage(), e);

        return strategies.stream()
                         .filter(strategy -> strategy.supports(e))
                         .findFirst()
                         .map(strategy -> strategy.handleException(e))
                         .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                        .body(CommonDto.<Void>builder()
                                                                       .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                                       .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                                                       .message("알 수 없는 Feign 오류 발생: " + e.getMessage())
                                                                       .data(null)
                                                                       .build()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleValidationException(MethodArgumentNotValidException e) {
        log.error(METHOD_ARGUMENT_NOT_VALID_EXCEPTION, e.getMessage(), e);

        return CommonDto.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("입력값이 유효하지 않습니다.")
                        .data(e.getBindingResult()
                               .getFieldErrors()) // 어떤 필드가 오류인지 포함
                        .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleConstraintViolationException(ConstraintViolationException e) {
        log.error(CONSTRAINT_VIOLATION_EXCEPTION, e.getMessage(), e);

        return CommonDto.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("입력값이 유효하지 않습니다.")
                        .data(e.getConstraintViolations()) // 어떤 제약 조건이 실패했는지 포함
                        .build();
    }

    @ExceptionHandler(IllegalAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonDto<Object> handleForbiddenException(IllegalAccessException e) {
        log.error(FORBIDDEN_EXCEPTION, e.getMessage(), e);

        return CommonDto.builder()
                        .status(HttpStatus.FORBIDDEN)
                        .code(HttpStatus.FORBIDDEN.value())
                        .message("권한이 없습니다.")
                        .data(null)
                        .build();
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonDto<Object> handleDatabaseException(DataAccessException e) {
        log.error(DATABASE_EXCEPTION, e.getMessage(), e);

        return CommonDto.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("데이터베이스 오류가 발생했습니다.")
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
