package com.owlexpress.product.common.exceptions;

import com.owlexpress.product.common.CommonDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.owlexpress.product.common.exceptions.ProductException.*;
import static com.owlexpress.product.common.exceptions.ProductException.ProductNameDuplicateExceptoin;
import static com.owlexpress.product.common.exceptions.ProductException.ProductNotFoundException;

@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionAdvice {


    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleProductNotFoundException(ProductNotFoundException e) {
        log.error("error ={}", e.getMessage(), e);
        return CommonDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage()) // Ensure the message is included
                .data(null)
                .build();
    }

    @ExceptionHandler(ProductNameDuplicateExceptoin.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleProductNotFoundException(ProductNameDuplicateExceptoin e) {
        log.error("error ={}", e.getMessage(), e);

        return CommonDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage()) // Ensure the message is included
                .data(null)
                .build();
    }
    @ExceptionHandler(HubInfoNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleHubInfoNotFoundException(HubInfoNotFoundException e) {
        log.error("error ={}", e.getMessage(), e);

        return CommonDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage()) // Ensure the message is included
                .data(null)
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleValidationException(MethodArgumentNotValidException e) {
        log.error("error ={}", e.getMessage(), e);

        return CommonDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message("입력값이 유효하지 않습니다.")
                .data(e.getBindingResult().getFieldErrors()) // 어떤 필드가 오류인지 포함
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("error ={}", e.getMessage(), e);

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
        log.error("error ={}", e.getMessage(), e);

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
        log.error("error ={}", e.getMessage(), e);

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
        log.error("error ={}", e.getMessage(), e);

        return CommonDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("예기치 않은 오류가 발생했습니다.")
                .data(null)
                .build();
    }

}
