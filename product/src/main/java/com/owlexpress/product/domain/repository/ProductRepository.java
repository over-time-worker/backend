package com.owlexpress.product.domain.repository;

import com.owlexpress.product.domain.entity.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);

    Optional<Product> findById(UUID productsId);
}
