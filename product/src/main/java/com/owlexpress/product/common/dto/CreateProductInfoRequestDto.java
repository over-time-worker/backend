package com.owlexpress.product.common.dto;

import com.owlexpress.product.domain.constant.ProductType;
import com.owlexpress.product.domain.entity.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateProductInfoRequestDto {
    @NotNull(message = "상품ID는 필수값입니다.")
    private UUID productId;
    @Size(min = 1,max = 50)
    @NotNull(message = "상품명은 필수값입니다.")
    private String productName;
    @NotNull(message = "상품 가격은 필수값입니다.")
    private BigDecimal productPrice;
    @NotNull(message = "상품 타입은 필수값입니다(NORMAL, FRESH)")
    private ProductType productType;
    @NotNull(message = "생성업체 ID는 필수값입니다.")
    private UUID producerId;

    @Builder
    public CreateProductInfoRequestDto(
            UUID productId,
            String productName,
            BigDecimal productPrice,
            ProductType productType,
            UUID producerId
    ) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productType = productType;
        this.producerId = producerId;
    }

    public static CreateProductInfoRequestDto fromEntity(Product product) {
        return CreateProductInfoRequestDto.builder()
                                   .productId(product.getProductId())
                                   .productName(product.getProductName())
                                   .productPrice(product.getProductPrice())
                                   .productType(product.getProductType())
                                   .producerId(product.getProducerId())
                                   .build();
    }
}