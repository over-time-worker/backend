package com.owlexpress.producer.presentation.controller;

import com.owlexpress.producer.application.usecase.ProducerUsecase;
import com.owlexpress.producer.common.CommonDto;
import com.owlexpress.producer.presentation.dto.request.CreateProducerRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/producers")
public class ProducerController {
    private final ProducerUsecase producerUsecase;

    @PostMapping()
    public ResponseEntity<CommonDto<Object>> create(
            //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @Valid @RequestBody CreateProducerRequestDto createProducerRequestDto
    ) {
        producerUsecase.create(createProducerRequestDto);

        CommonDto<Object> commonDto = CommonDto.builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message("생성업체 등록 성공")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commonDto);
    }


}
