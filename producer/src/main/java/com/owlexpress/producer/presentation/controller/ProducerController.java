package com.owlexpress.producer.presentation.controller;

import com.owlexpress.producer.application.usecase.ProducerUsecaseImpl;
import com.owlexpress.producer.common.CommonDto;
import com.owlexpress.producer.common.dto.request.CreateProducerRequestDto;
import com.owlexpress.producer.common.dto.request.UpdateProducerRequestDto;
import com.owlexpress.producer.domain.service.ProducerService;
import com.owlexpress.producer.presentation.ProducerUsecase;
import com.owlexpress.producer.presentation.dto.response.ProducerResponseDto;
import com.owlexpress.producer.presentation.dto.response.SearchProducerResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/producers")
@Slf4j
public class ProducerController {
    private final ProducerUsecase producerUsecase;
    private final ProducerService producerService;

    @PostMapping()
    public ResponseEntity<CommonDto<Void>> create(
            @RequestHeader("X-User-Passport") String passport,
            @RequestBody CreateProducerRequestDto createProducerRequestDto
    ) {
        producerUsecase.create(createProducerRequestDto, passport);
        log.info("passport= {}", passport);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.CREATED)
                                             .code(HttpStatus.CREATED.value())
                                             .message("생성 업체 등록 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(commonDto);
    }

    @PutMapping("/{producerId}")
    public ResponseEntity<CommonDto<Void>> update(
            @RequestHeader("X-User-Passport") String passport,
            @RequestBody UpdateProducerRequestDto updateProducerRequestDto,
            @PathVariable UUID producerId

    ) {
        producerUsecase.update(
                updateProducerRequestDto,
                producerId,
                passport
        );
        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.ACCEPTED)
                                             .code(HttpStatus.ACCEPTED.value())
                                             .message("생성 업체 수정 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED)
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
                                                            .message("생성 업체 조회 성공")
                                                            .data(producerResponseDto)
                                                            .build();


        return ResponseEntity.status(HttpStatus.OK)
                             .body(commonDto);
    }

    @GetMapping("/search")
    public ResponseEntity<CommonDto<PagedModel<SearchProducerResponseDto>>> search(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "desc") String sort,
            @RequestParam(name = "q", defaultValue = "") String q,
            @RequestParam(name = "orderBy", defaultValue = "createdAt") String orderBy
    ) {
        PagedModel<SearchProducerResponseDto> searchResult = producerService.search(
                page,
                size,
                sort,
                q,
                orderBy
        );

        CommonDto<PagedModel<SearchProducerResponseDto>> commonDto = CommonDto.<PagedModel<SearchProducerResponseDto>>builder()
                                                                              .status(HttpStatus.OK)
                                                                              .code(HttpStatus.OK.value())
                                                                              .message("생성 업체 검색 성공")
                                                                              .data(searchResult)
                                                                              .build();

        return ResponseEntity.status(HttpStatus.OK)
                             .body(commonDto);
    }

    @DeleteMapping("/{producerId}")
    public ResponseEntity<CommonDto<Void>> delete(
           @RequestHeader("X-User-Passport") String passport,
            @PathVariable UUID producerId
    ) {
        producerUsecase.delete(producerId,passport);
        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.ACCEPTED)
                                             .code(HttpStatus.ACCEPTED.value())
                                             .message("생성 업체 삭제 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                             .body(commonDto);

    }


}
