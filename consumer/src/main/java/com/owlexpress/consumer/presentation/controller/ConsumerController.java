package com.owlexpress.consumer.presentation.controller;

import com.owlexpress.consumer.application.usecase.ConsumerUsecase;
import com.owlexpress.consumer.common.dto.CommonDto;
import com.owlexpress.consumer.common.dto.request.CreateConsumerRequestDto;
import com.owlexpress.consumer.common.dto.request.UpdateConsumerRequestDto;
import com.owlexpress.consumer.common.exceptions.ConsumerException;
import com.owlexpress.consumer.domain.service.ConsumerService;
import com.owlexpress.consumer.common.dto.response.ConsumerResponseDto;
import com.owlexpress.consumer.common.dto.response.SearchConsumerResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/consumers")
public class ConsumerController {
    private final ConsumerUsecase consumerUsecase;
    private final ConsumerService consumerService;

    @PostMapping
    public ResponseEntity<CommonDto<Void>> create(
           @RequestHeader("X-User-Passport") String passport,
           @Valid @RequestBody CreateConsumerRequestDto consumerRequestDto
    ) {
        consumerUsecase.create(consumerRequestDto,passport);
        //메서드 넣기

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.CREATED)
                                             .code(HttpStatus.CREATED.value())
                                             .message("수령업체 생성 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(commonDto);
    }

    @PutMapping("/{consumerId}")
    public ResponseEntity<CommonDto<Void>> update(
             @RequestHeader("X-User-Passport") String passport,
            @PathVariable UUID consumerId,
            @Valid @RequestBody UpdateConsumerRequestDto updateConsumerRequestDto
    ) {

        //메서드 넣기
        consumerUsecase.update(consumerId, updateConsumerRequestDto,passport);


        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.ACCEPTED)
                                             .code(HttpStatus.ACCEPTED.value())
                                             .message("수령업체 수정 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                             .body(commonDto);
    }

    @GetMapping("/{consumerId}")
    public ResponseEntity<CommonDto<ConsumerResponseDto>> find(
            @PathVariable UUID consumerId
    ) {
        //메서드 넣기
        ConsumerResponseDto consumerResponseDto = consumerService.find(consumerId);

        CommonDto<ConsumerResponseDto> commonDto = CommonDto.<ConsumerResponseDto>builder()
                                          .status(HttpStatus.OK)
                                          .code(HttpStatus.OK.value())
                                          .message("수령 업체 조회 성공")
                                          .data(consumerResponseDto)
                                          .build();

        return ResponseEntity.status(HttpStatus.OK)
                             .body(commonDto);
    }

    @GetMapping("/search")
    public ResponseEntity<CommonDto<PagedModel<SearchConsumerResponseDto>>> search(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "desc") String sort,
            @RequestParam(name = "q", defaultValue = "") String q,
            @RequestParam(name = "orderBy", defaultValue = "createdAt") String orderBy
    ) {

        PagedModel<SearchConsumerResponseDto> searchResult = consumerService.search(
                page,
                size,
                sort,
                q,
                orderBy
        );

        CommonDto<PagedModel<SearchConsumerResponseDto>> commonDto = CommonDto.<PagedModel<SearchConsumerResponseDto>>builder()
                                                      .status(HttpStatus.OK)
                                                      .code(HttpStatus.OK.value())
                                                      .message("수령업체 검색 성공")
                                                      .data(searchResult)
                                                      .build();

        return ResponseEntity.status(HttpStatus.OK)
                             .body(commonDto);
    }

    @DeleteMapping("/{consumerId}")
    public ResponseEntity<CommonDto<Void>> delete(
            @RequestHeader("X-User-Passport") String passport,
            @PathVariable UUID consumerId
    ) throws ConsumerException.ConsumerDeliveryException {
        consumerUsecase.delete(consumerId,passport);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.ACCEPTED)
                                             .code(HttpStatus.ACCEPTED.value())
                                             .message("수령업체 삭제 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                             .body(commonDto);
    }
}
