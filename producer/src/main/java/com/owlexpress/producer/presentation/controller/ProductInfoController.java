package com.owlexpress.producer.presentation.controller;

import com.owlexpress.producer.common.CommonDto;
import com.owlexpress.producer.domain.service.ProductInfoService;
import com.owlexpress.producer.presentation.dto.request.CreateProductInfoRequestDto;
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
@RequestMapping("/api/producers/product-info")
public class ProductInfoController {
    private final ProductInfoService productInfoService;

    @PostMapping
    public ResponseEntity<CommonDto<Void>> create(
            //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @Valid @RequestBody CreateProductInfoRequestDto createProductInfoRequestDto
    ) {

        productInfoService.create(createProductInfoRequestDto);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.CREATED)
                                             .code(HttpStatus.CREATED.value())
                                             .message("상품 정보 등록 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commonDto);
    }
}
