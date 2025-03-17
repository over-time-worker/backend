package com.owlexpress.product.presentation;

import com.owlexpress.product.application.ProductHubUsecase;
import com.owlexpress.product.common.CommonDto;
import com.owlexpress.product.domain.service.ProductDomainService;
import com.owlexpress.product.presentation.dto.request.CreateProductRequestDto;
import com.owlexpress.product.presentation.dto.request.UpdateProductDto;
import com.owlexpress.product.application.dto.response.FindProductResponseDto;
import com.owlexpress.product.application.dto.response.SearchProductResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductDomainService productDomainService;
    private final ProductHubUsecase productHubUsecase;

    @PostMapping
    public ResponseEntity<CommonDto<Void>> create(
//TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @Valid @RequestBody CreateProductRequestDto createProductRequestDto
    ) {
        productHubUsecase.createProduct(createProductRequestDto);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .message("상품 생성 성공")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commonDto);
    }

    @PutMapping("/{productsId}")
    public ResponseEntity<CommonDto<Void>> update(
            //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @Valid @RequestBody UpdateProductDto updateProductDto,
            @PathVariable UUID productsId
    ){

        productDomainService.updateProduct(updateProductDto,productsId);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message("상품 수정 성공")
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }

    @GetMapping("/{productsId}")
    public ResponseEntity<CommonDto<FindProductResponseDto>> get(
            //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @PathVariable UUID productsId
    ){
        FindProductResponseDto findProductResponseDto = productHubUsecase.find(productsId);

        CommonDto<FindProductResponseDto> commonDto = CommonDto.<FindProductResponseDto>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .message("상품 조회 성공")
                .data(findProductResponseDto)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(commonDto);
    }

    @GetMapping("/search")
    public ResponseEntity<CommonDto<PagedModel<SearchProductResponseDto>>> search(
            @RequestParam(name = "page",defaultValue = "0") Integer page,
            @RequestParam(name = "size",defaultValue = "10") Integer size,
            @RequestParam(name = "sort",required = false,defaultValue = "desc") String sort,
            @RequestParam(name = "q", defaultValue = "") String q,
            @RequestParam(name = "orderBy",defaultValue = "createdAt") String orderBy
    ) {
        PagedModel<SearchProductResponseDto> searchResult = productHubUsecase.search(page,size,sort, q,orderBy);

        CommonDto<PagedModel<SearchProductResponseDto>> commonDto = CommonDto.
                <PagedModel<SearchProductResponseDto>>builder().
                status(HttpStatus.OK).
                code(HttpStatus.OK.value()).
                message("상품 검색 성공").
                data(searchResult).
                build();

        return ResponseEntity.status(HttpStatus.OK).body(commonDto);
    }

    @DeleteMapping("/{productsId}")
    public ResponseEntity<CommonDto<Void>> delete(
            @PathVariable UUID productsId
    ){

        productDomainService.delete(productsId);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message("상품 삭제 성공")
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }

}
