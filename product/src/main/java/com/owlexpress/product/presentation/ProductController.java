package com.owlexpress.product.presentation;

import com.owlexpress.product.common.CommonDto;
import com.owlexpress.product.domain.service.ProductDomainService;
import com.owlexpress.product.presentation.dto.request.CreateProductRequestDto;
import com.owlexpress.product.presentation.dto.request.UpdateProductDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductDomainService productDomainService;

    @PostMapping
    public ResponseEntity<CommonDto> create(
//TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @Valid @RequestBody CreateProductRequestDto createProductRequestDto
    ) {
        productDomainService.createProduct(createProductRequestDto);

        CommonDto<Object> commonDto = CommonDto.builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .message("상품 생성 성공")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commonDto);
    }

    @PutMapping("/{productsId}")
    public ResponseEntity<CommonDto> update(
            //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @Valid @RequestBody UpdateProductDto updateProductDto,
            @PathVariable UUID productsId
    ){

        productDomainService.updateProduct(updateProductDto,productsId);

        CommonDto<Object> commonDto = CommonDto.builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message("상품 수정 성공")
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }

}
