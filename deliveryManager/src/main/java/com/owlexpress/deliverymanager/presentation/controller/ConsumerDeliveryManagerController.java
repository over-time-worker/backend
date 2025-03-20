package com.owlexpress.deliverymanager.presentation.controller;

import com.owlexpress.deliverymanager.application.usecase.ConsumerDeliveryManagerUsecase;
import com.owlexpress.deliverymanager.infrastructure.CommonDto;
import com.owlexpress.deliverymanager.presentation.dto.request.CreateConsumerDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.request.CreateHubDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.request.UpdateConsumerDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.request.UpdateHubDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.response.FindConsumerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/consumer/delivery")
public class ConsumerDeliveryManagerController {

    private final ConsumerDeliveryManagerUsecase consumerDeliveryManagerUsecase;

    @PostMapping
    public ResponseEntity<CommonDto<Void>> create(
            CreateConsumerDeliveryManagerRequestDto createConsumerDeliveryManagerRequestDto
    ) {

        consumerDeliveryManagerUsecase.create(consumerDeliveryManagerUsecase);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.CREATED)
                                             .code(HttpStatus.CREATED.value())
                                             .message("업체 배송 관리자 생성 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commonDto);
    }

    @PutMapping("/{consumerDeliveryManagerId")
    public ResponseEntity<CommonDto<Void>> update(
            @PathVariable("consumerDeliveryManagerId") UUID consumerDeliveryManagerId,
            @RequestBody UpdateConsumerDeliveryManagerRequestDto updateConsumerDeliveryManagerRequestDto
    ) {
        consumerDeliveryManagerUsecase.update(updateConsumerDeliveryManagerRequestDto, consumerDeliveryManagerId);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.ACCEPTED)
                                             .code(HttpStatus.ACCEPTED.value())
                                             .message("업체 배송 관리자 수정 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }

    @GetMapping("/{consumerDeliveryManagerId}")
    public ResponseEntity<CommonDto<FindConsumerResponseDto>> find(
            //TODO:: passport 연결 후에는 UUID받을 필요 없음
            @PathVariable("consumerDeliveryManagerId") UUID consumerDeliveryManagerId
    ){
        FindConsumerResponseDto findConsumerResponseDto = consumerDeliveryManagerUsecase.find(consumerDeliveryManagerId);

        CommonDto<FindConsumerResponseDto> commonDto = CommonDto.<FindConsumerResponseDto>builder()
                                         .status(HttpStatus.OK)
                                         .code(HttpStatus.OK.value())
                                         .message("업체 배송 관리자 조회 성공")
                                         .data(findConsumerResponseDto)
                                         .build();

        return ResponseEntity.status(HttpStatus.OK).body(commonDto);
    }

    @GetMapping("/search")
    public ResponseEntity<CommonDto<PagedModel<FindConsumerResponseDto>>> search(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "desc") String sort,
            @RequestParam(name = "q", defaultValue = "") String q,
            @RequestParam(name = "orderBy", defaultValue = "createdAt") String orderBy
    ) {
        PagedModel<FindConsumerResponseDto> searchResult = consumerDeliveryManagerUsecase.search(page, size, sort, q, orderBy);

        CommonDto<PagedModel<FindConsumerResponseDto>> commonDto = CommonDto.<PagedModel<FindConsumerResponseDto>>builder()
                                                     .status(HttpStatus.OK)
                                                     .code(HttpStatus.OK.value())
                                                     .message("허브 배송관리자 조회 성공")
                                                     .data(searchResult)
                                                     .build();

        return ResponseEntity.status(HttpStatus.OK)
                             .body(commonDto);
    }

    @DeleteMapping("/{consumerDeliveryManagerId}")
    public ResponseEntity<CommonDto<Void>> delete(
            @PathVariable("consumerDeliveryManagerId") UUID consumerDeliveryManagerId
    ){

        consumerDeliveryManagerUsecase.delete(consumerDeliveryManagerId);
        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.ACCEPTED)
                                             .code(HttpStatus.ACCEPTED.value())
                                             .message("업체 배송 관리자 삭제 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }
}
