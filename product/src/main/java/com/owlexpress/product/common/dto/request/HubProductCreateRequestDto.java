package com.owlexpress.product.common.dto.request;

import com.owlexpress.product.domain.constant.ProductType;
import com.owlexpress.product.domain.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubProductCreateRequestDto {

    @NotNull(message = "상품 아이디는 비어있을 수 없습니다.")
    private UUID productId;

    @NotNull(message = "허브 아이디는 비어있을 수 없습니다.")
    private UUID hubId;

    @NotNull(message = "생산 업체 아이디는 비어있을 수 없습니다.")
    private UUID producerId;
    
    @NotBlank(message = "생산 업체명은 비어있을 수 없습니다")
    private String producerName;

    @NotNull(message = "상품명은 비어있을 수 없습니다")
    private String productName;

    @NotNull(message = "상품 타입은 비어있을 수 없습니다.")
    private ProductType productType;

    @NotNull(message = "상품 재고는 비어있을 수 없습니다.")
    @Positive(message = "상품 재고는 1개 이상이여야 합니다.")
    private Long productStock;

    @Builder
    public HubProductCreateRequestDto(
            UUID productId,
            UUID hubId,
            UUID producerId,
            String producerName,
            String productName,
            ProductType productType,
            Long productStock
    ) {
        this.productId = productId;
        this.hubId = hubId;
        this.producerId = producerId;
        this.producerName = producerName;
        this.productName = productName;
        this.productType = productType;
        this.productStock = productStock;
    }

    public static HubProductCreateRequestDto toDto(Product product,
                                                   UUID hubId
    ) {
       return HubProductCreateRequestDto.builder()
                .productId(product.getProductId())
                .hubId(hubId)
                .producerId(product.getProducerId())
                .producerName(product.getProducerName())
                .productName(product.getProductName())
                .productType(product.getProductType())
                .productStock(2000L)
                .build();
    }
}
