package com.owlexpress.producer.infrastructure.repository.productInfo;

import com.owlexpress.producer.domain.entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductInfoJpaRepository extends JpaRepository<ProductInfo, Long> {

    Optional<ProductInfo> findByProductName(String productName);
}
