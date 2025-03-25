package com.owlexpress.producer.application.dto.request;

import com.owlexpress.producer.common.constant.ProductType;
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
}