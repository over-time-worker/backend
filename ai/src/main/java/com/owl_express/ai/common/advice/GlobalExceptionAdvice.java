package com.owl_express.ai.common.advice;

import com.owl_express.ai.application.dtos.CommonDto;
import com.owl_express.ai.application.exceptions.AiException.MessageNotFoundException;
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

    private static final String METHOD_ARGUMENT_NOT_VALID_EXCEPTION = "handleMethodArgumentNotValidException : {}";
    private static final String CONSTRAINT_VIOLATION_EXCEPTION = "handleConstraintViolationException : {}";
    private static final String MESSAGE_NOT_FOUND_EXCEPTION = "handleMessageNotFoundException : {}";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonDto<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder sb = new StringBuilder();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            sb.append(error.getDefaultMessage());
        });

       log.error(METHOD_ARGUMENT_NOT_VALID_EXCEPTION, sb.toString());

        return CommonDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .code(HttpStatus.BAD_REQUEST.value())
                .message(sb.toString())
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

    @ExceptionHandler(MessageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonDto<Object> handleMessageNotFoundException(MessageNotFoundException e) {

        log.error(MESSAGE_NOT_FOUND_EXCEPTION, e.getMessage());

        return CommonDto.builder()
                .status(HttpStatus.NOT_FOUND)
                .code(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .data(null)
                .build();

    }

}
