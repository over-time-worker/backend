package com.owlexpress.producer.presentation.controller;

import com.owlexpress.producer.application.usecase.ProducerUsecase;
import com.owlexpress.producer.common.CommonDto;
import com.owlexpress.producer.common.dto.request.CreateProducerRequestDto;
import com.owlexpress.producer.common.dto.request.UpdateProductRequestDto;
import com.owlexpress.producer.domain.service.ProducerService;
import com.owlexpress.producer.presentation.dto.response.ProducerResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/producers")
public class ProducerController {
    private final ProducerUsecase producerUsecase;
    private final ProducerService producerService;

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

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(commonDto);
    }

    @PutMapping("/{producerId}")
    public ResponseEntity<CommonDto<Object>> update(
            //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @Valid @RequestBody UpdateProductRequestDto updateProductRequestDto,
            @PathVariable UUID producerId

    ) {
        producerUsecase.update(
                updateProductRequestDto,
                producerId
        );
        CommonDto<Object> commonDto = CommonDto.builder()
                                               .status(HttpStatus.ACCEPTED)
                                               .code(HttpStatus.ACCEPTED.value())
                                               .message("생성업체 수정 성공")
                                               .build();


        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(commonDto);
    }

    @GetMapping("/{producerId}")
    public ResponseEntity<CommonDto<ProducerResponseDto>> find(
            @PathVariable UUID producerId
    ) {

        ProducerResponseDto producerResponseDto = producerService.find(producerId);

        CommonDto<ProducerResponseDto> commonDto = CommonDto.<ProducerResponseDto>builder()
                                                            .status(HttpStatus.OK)
                                                            .code(HttpStatus.OK.value())
                                                            .message("생성업체 조회 성공")
                                                            .data(producerResponseDto)
                                                            .build();


        return ResponseEntity.status(HttpStatus.OK)
                             .body(commonDto);
    }


}
