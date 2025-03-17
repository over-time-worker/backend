package com.owlexpress.producer.presentation.dto.request;

import com.owlexpress.producer.domain.entity.Producer;
import com.owlexpress.producer.domain.entity.ProductInfo;
import com.owlexpress.producer.domain.entity.constant.ProductType;
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

    public static ProductInfo toEntity(CreateProductInfoRequestDto createProductInfoRequestDto,
                                       Producer producer
                                       ) {
        return ProductInfo.builder()
                .productId(createProductInfoRequestDto.getProductId())
                .productName(createProductInfoRequestDto.getProductName())
                .productPrice(createProductInfoRequestDto.getProductPrice())
                .productType(createProductInfoRequestDto.getProductType())
                .producer(producer)
                .build();
    }
}
