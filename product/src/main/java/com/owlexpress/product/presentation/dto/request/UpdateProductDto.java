package com.owlexpress.product.presentation.dto.request;

import com.owlexpress.product.domain.constant.ProductType;
import com.owlexpress.product.domain.entity.Product;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateProductDto {
    @Size(min = 1, max = 50, message = "[size:productName]")
    private String productName;

    private ProductType productType;

    private BigDecimal productPrice;

    private UUID producerId;

    @Size(min = 1, max = 50)
    private String producerName;

    @Size(min=1, max = 255)
    private String producerAddress;

    @Builder
    public UpdateProductDto(
            String productName,
            ProductType productType,
            BigDecimal productPrice,
            UUID producerId,
            String producerName,
            String producerAddress
    )
    {
        this.productName = productName;
        this.productType = productType;
        this.productPrice = productPrice;
        this.producerId = producerId;
        this.producerName = producerName;
        this.producerAddress = producerAddress;
    }

    public static void updateProduct(Product product, UpdateProductDto updateProductDto) {
        if (updateProductDto.getProductName() != null) {
            product.setProductName(updateProductDto.getProductName());
        }
        if (updateProductDto.getProductPrice() != null) {
            product.setProductPrice(updateProductDto.getProductPrice());
        }
        if (updateProductDto.getProductType() != null) {
            product.setProductType(updateProductDto.getProductType());
        }
        if (updateProductDto.getProducerId() != null) {
            product.setProducerId(updateProductDto.getProducerId());
        }
        if (updateProductDto.getProducerName() != null) {
            product.setProducerName(updateProductDto.getProducerName());
        }
        if (updateProductDto.getProducerAddress() != null) {
            product.setProducerAddress(updateProductDto.getProducerAddress());
        }
    }
}
