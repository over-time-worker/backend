package com.owlexpress.producer.infrastructure.repository.productInfo;

import com.owlexpress.producer.domain.entity.ProductInfo;
import com.owlexpress.producer.domain.repository.ProductInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductInfoRepositoryImpl implements ProductInfoRepository {
    private final ProductInfoJpaRepository productInfoJpaRepository;

    @Override
    public Optional<ProductInfo> findByProductNmae(String productName) {
        return productInfoJpaRepository.findByProductName(productName);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return productInfoJpaRepository.save(productInfo);
    }
}
