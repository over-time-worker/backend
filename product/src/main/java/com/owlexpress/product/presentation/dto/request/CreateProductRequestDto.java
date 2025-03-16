package com.owlexpress.product.presentation.dto.request;

import com.owlexpress.product.domain.constant.ProductType;
import com.owlexpress.product.domain.entity.Product;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CreateProductRequestDto {
    @Size(min = 1, max = 50, message = "[size:productName]")
    private String productName;

    private ProductType productType;

    private BigDecimal productPrice;

    private UUID producerId;

    @Size(min = 1, max = 50)
    private String producerName;

    @Size(min=1, max = 255)
    private String producerAddress;

    public CreateProductRequestDto(
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

    public static Product toEntity(CreateProductRequestDto createProductRequestDto) {
        return Product.builder()
                .productName(createProductRequestDto.getProductName())
                .productPrice(createProductRequestDto.getProductPrice())
                .productType(createProductRequestDto.getProductType())
                .producerId(createProductRequestDto.getProducerId())
                .producerName(createProductRequestDto.getProducerName()) //TODO:: Clinet 연결 후 필드 삭제
                .producerAddress(createProductRequestDto.getProducerAddress()) //TODO:: Clinet 연결 후 필드 삭제
                .build();
    }


}
