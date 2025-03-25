package com.owlexpress.product.presentation;

import com.owlexpress.product.application.ProductUsecaseImpl;
import com.owlexpress.product.application.dto.response.FindProductResponseDto;
import com.owlexpress.product.application.dto.response.SearchProductResponseDto;
import com.owlexpress.product.common.CommonDto;
import com.owlexpress.product.common.exceptions.ProductException;
import com.owlexpress.product.domain.service.ProductDomainService;
import com.owlexpress.product.presentation.dto.request.CreateProductRequestDto;
import com.owlexpress.product.presentation.dto.request.UpdateProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductDomainService productDomainService;
    private final ProductUsecase productUsecase;

    @PostMapping
    public ResponseEntity<CommonDto<Void>> create(
            @RequestHeader("X-User-Passport") String passport,
            @RequestBody CreateProductRequestDto createProductRequestDto
    ) throws ProductException.ProductCreateFailException {
        productUsecase.createProduct(createProductRequestDto, passport);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.CREATED)
                                             .code(HttpStatus.CREATED.value())
                                             .message("상품 생성 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(commonDto);
    }

    @PutMapping("/{productsId}")
    public ResponseEntity<CommonDto<Void>> update(
            @RequestHeader("X-User-Passport") String passport,
            @RequestBody UpdateProductDto updateProductDto,
            @PathVariable UUID productsId
    ) throws ProductException.ProductUpdateFailException {

        productUsecase.updateProduct(updateProductDto, productsId, passport);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.ACCEPTED)
                                             .code(HttpStatus.ACCEPTED.value())
                                             .message("상품 수정 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                             .body(commonDto);
    }

    @GetMapping("/{productsId}")
    public ResponseEntity<CommonDto<FindProductResponseDto>> get(
            @RequestHeader("X-User-Passport") String passport,
            @PathVariable UUID productsId
    ) {
        FindProductResponseDto findProductResponseDto = productUsecase.find(productsId);

        CommonDto<FindProductResponseDto> commonDto = CommonDto.<FindProductResponseDto>builder()
                                                               .status(HttpStatus.OK)
                                                               .code(HttpStatus.OK.value())
                                                               .message("상품 조회 성공")
                                                               .data(findProductResponseDto)
                                                               .build();

        return ResponseEntity.status(HttpStatus.OK)
                             .body(commonDto);
    }

    @GetMapping("/search")
    public ResponseEntity<CommonDto<PagedModel<SearchProductResponseDto>>> search(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "desc") String sort,
            @RequestParam(name = "q", defaultValue = "") String q,
            @RequestParam(name = "orderBy", defaultValue = "createdAt") String orderBy
    ) {
        PagedModel<SearchProductResponseDto> searchResult = productUsecase.search(page, size, sort, q, orderBy);

        CommonDto<PagedModel<SearchProductResponseDto>> commonDto = CommonDto.<PagedModel<SearchProductResponseDto>>builder()
                                                                             .status(HttpStatus.OK)
                                                                             .code(HttpStatus.OK.value())
                                                                             .message("상품 검색 성공")
                                                                             .data(searchResult)
                                                                             .build();

        return ResponseEntity.status(HttpStatus.OK)
                             .body(commonDto);
    }

    @DeleteMapping("/{productsId}")
    public ResponseEntity<CommonDto<Boolean>> delete(
            @RequestHeader("X-User-Passport") String passport,
            @PathVariable UUID productsId
    ) {

        productUsecase.delete(productsId, passport);

        CommonDto<Boolean> commonDto = CommonDto.<Boolean>builder()
                                                .status(HttpStatus.ACCEPTED)
                                                .code(HttpStatus.ACCEPTED.value())
                                                .message("상품 삭제 성공")
                                                .data(true)
                                                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                             .body(commonDto);
    }

}
