package com.owlexpress.product.application.dto.response;

import com.owlexpress.product.domain.constant.ProductType;
import com.owlexpress.product.domain.entity.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindProductResponse {
    private UUID productId;
    private String productName;
    private BigDecimal productPrice;
    private ProductType productType;
    private int totalQuantity;
    private UUID producerId;
    private String producerName;
    private String producerAddress;


    @Builder
    public FindProductResponse(
            UUID productId,
            String productName,
            BigDecimal productPrice,
            ProductType productType,
            int totalQuantity,
            UUID producerId,
            String producerName,
            String producerAddress
    )
    {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productType = productType;
        this.totalQuantity = totalQuantity;
        this.producerId = producerId;
        this.producerName = producerName;
        this.producerAddress = producerAddress;
    }


    public static FindProductResponse toDTO(Product product, AtomicInteger totalQuantity) {
        return FindProductResponse.builder()
                .productName(product.getProductName())
                .productId(product.getProductId())
                .productType(product.getProductType())
                .productPrice(product.getProductPrice())
                .totalQuantity(totalQuantity.get())
                .producerId(product.getProducerId())
                .producerName(product.getProducerName())
                .producerAddress(product.getProducerAddress())
                .build();
    }
}
