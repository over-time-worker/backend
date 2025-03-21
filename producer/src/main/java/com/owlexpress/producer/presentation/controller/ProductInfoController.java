package com.owlexpress.producer.presentation.controller;

import com.owlexpress.producer.common.CommonDto;
import com.owlexpress.producer.domain.service.ProductInfoService;
import com.owlexpress.producer.presentation.dto.request.CreateProductInfoRequestDto;
import com.owlexpress.producer.presentation.dto.request.UpdateProductInfoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/producers/product-info")
public class ProductInfoController {
    private final ProductInfoService productInfoService;

    @PostMapping
    public ResponseEntity<CommonDto<Void>> create(
            @RequestHeader("X-User-Passport") String passport,
            @RequestBody CreateProductInfoRequestDto createProductInfoRequestDto
    ) {

        productInfoService.create(createProductInfoRequestDto,passport);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.CREATED)
                                             .code(HttpStatus.CREATED.value())
                                             .message("상품 정보 등록 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commonDto);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<CommonDto<Void>> update(
            @RequestHeader("X-User-Passport") String passport,
            @PathVariable UUID productId,
            @RequestBody UpdateProductInfoRequestDto updateProductInfoRequestDto
    ){
        productInfoService.update(productId,updateProductInfoRequestDto,passport);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.ACCEPTED)
                                             .code(HttpStatus.ACCEPTED.value())
                                             .message("상품 정보 수정 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);

    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<CommonDto<Void>> delete(
            @RequestHeader("X-User-Passport") String passport,
            @PathVariable UUID productId
    ){
        productInfoService.delete(productId,passport);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.ACCEPTED)
                                             .code(HttpStatus.ACCEPTED.value())
                                             .message("상품 정보 삭제 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }

}
