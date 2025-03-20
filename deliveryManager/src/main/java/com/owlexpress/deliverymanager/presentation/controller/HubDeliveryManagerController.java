package com.owlexpress.deliverymanager.presentation.controller;

import com.owlexpress.deliverymanager.application.usecase.HubDeliveryManagerUsecase;
import com.owlexpress.deliverymanager.common.exception.HubDeliveryManagerException;
import com.owlexpress.deliverymanager.infrastructure.CommonDto;
import com.owlexpress.deliverymanager.presentation.dto.request.CreateHubDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.request.UpdateHubDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.response.FindHubDeliveryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hub/delivery")
public class HubDeliveryManagerController {

    private final HubDeliveryManagerUsecase hubDeliveryManagerUsecase;

    @PostMapping
    public ResponseEntity<CommonDto<Void>> create(
            @RequestHeader(name = "X-User-Passport") String passport,
            CreateHubDeliveryManagerRequestDto createHubDeliveryManagerRequestDto
    ) {
        hubDeliveryManagerUsecase.create(createHubDeliveryManagerRequestDto);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.CREATED)
                                             .code(HttpStatus.CREATED.value())
                                             .message("허브 배송 관리자 생성 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commonDto);
    }

    @PutMapping("/{hubDeliveryManagerId}")
    public ResponseEntity<CommonDto<Void>> update(
            @PathVariable("hubDeliveryManagerId") UUID hubDeliveryManagerId,
            UpdateHubDeliveryManagerRequestDto updateHubDeliveryManagerRequestDto
    ) throws HubDeliveryManagerException.DuplicateAssignNumber {
        hubDeliveryManagerUsecase.update(updateHubDeliveryManagerRequestDto,hubDeliveryManagerId);


        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.ACCEPTED)
                                             .code(HttpStatus.ACCEPTED.value())
                                             .message("허브 배송 관리자 수정 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }

    @GetMapping("/{hubDeliveryManagerId}")
    public ResponseEntity<CommonDto<FindHubDeliveryResponseDto>> get(
            @PathVariable("hubDeliveryManagerId") UUID consumerDeliveryManagerId
    ){

        FindHubDeliveryResponseDto findHubDeliveryResponseDto = hubDeliveryManagerUsecase.find(consumerDeliveryManagerId);


        CommonDto<FindHubDeliveryResponseDto> commonDto = CommonDto.<FindHubDeliveryResponseDto>builder()
                                         .status(HttpStatus.OK)
                                         .code(HttpStatus.OK.value())
                                         .message("허브 배송 관리자 조회 성공")
                                         .data(findHubDeliveryResponseDto)
                                         .build();

        return ResponseEntity.status(HttpStatus.OK).body(commonDto);
    }

    @GetMapping("/search")
    public ResponseEntity<CommonDto<PagedModel<FindHubDeliveryResponseDto>>> search(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "desc") String sort,
            @RequestParam(name = "q", defaultValue = "") String q,
            @RequestParam(name = "orderBy", defaultValue = "createdAt") String orderBy
    ) {
        PagedModel<FindHubDeliveryResponseDto> searchResult = hubDeliveryManagerUsecase.search(page, size, sort, q, orderBy);

        CommonDto<PagedModel<FindHubDeliveryResponseDto>> commonDto = CommonDto.<PagedModel<FindHubDeliveryResponseDto>>builder()
                                                     .status(HttpStatus.OK)
                                                     .code(HttpStatus.OK.value())
                                                     .message("허브 배송관리자 조회 성공")
                                                     .data(searchResult)
                                                     .build();

        return ResponseEntity.status(HttpStatus.OK)
                             .body(commonDto);
    }

    @DeleteMapping("/{hubDeliveryManagerId}")
    public ResponseEntity<CommonDto<Void>> delete(
            @PathVariable("hubDeliveryManagerId") UUID hubDeliveryManagerId
    ) throws HubDeliveryManagerException.IsNotAvailableStatusException {
        hubDeliveryManagerUsecase.delete(hubDeliveryManagerId);


        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.ACCEPTED)
                                             .code(HttpStatus.ACCEPTED.value())
                                             .message("허브 배송 관리자 삭제 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }
}
