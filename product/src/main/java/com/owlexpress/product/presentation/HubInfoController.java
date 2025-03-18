package com.owlexpress.product.presentation;

import com.owlexpress.product.application.ProductUsecase;
import com.owlexpress.product.common.CommonDto;
import com.owlexpress.product.domain.entity.HubInfo;
import com.owlexpress.product.domain.service.HubInfoService;
import com.owlexpress.product.presentation.dto.request.CreateHubInfoRequestDto;
import com.owlexpress.product.presentation.dto.request.UpdateHubInfoRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products/hub-info")
public class HubInfoController {

    private final HubInfoService hubInfoService;
    private final ProductUsecase productUsecase;

    @PostMapping
    public ResponseEntity<CommonDto<Object>> create(
        //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
         @Valid @RequestBody CreateHubInfoRequestDto createHubInfoRequestDto
    ) {
        HubInfo hubInfo = hubInfoService.create(createHubInfoRequestDto);
        productUsecase.connect(createHubInfoRequestDto, hubInfo);

        CommonDto<Object> commonDto = CommonDto.builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .message("상품 생성 성공")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commonDto);
    }

    @PutMapping("/{hubInfoId}")
    public ResponseEntity<CommonDto<Object>> update(
            //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @Valid @RequestBody UpdateHubInfoRequestDto updateHubInfoRequestDto,
            @PathVariable UUID hubInfoId
    ){
        hubInfoService.update(hubInfoId, updateHubInfoRequestDto);

        CommonDto<Object> commonDto = CommonDto.builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message("상품 수정 성공")
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }

    @DeleteMapping("/{hubInfoId}")
    public ResponseEntity<CommonDto<Object>> delete(
            //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @PathVariable UUID hubInfoId
    ) {

        hubInfoService.delete(hubInfoId);

        CommonDto<Object> commonDto = CommonDto.builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message("상품 삭제 성공")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commonDto);
    }

}
