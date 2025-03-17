package com.owlexpress.producer.domain.repository;

import com.owlexpress.producer.domain.entity.ProductInfo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Optional;
import java.util.UUID;

public interface ProductInfoRepository {
    Optional<ProductInfo> findByProductNmae(@Size(min = 1,max = 50) @NotNull(message = "상품명은 필수값입니다.") String productName);

    ProductInfo save(ProductInfo productInfo);

    Optional<ProductInfo> findByProductId(UUID productId);
}
