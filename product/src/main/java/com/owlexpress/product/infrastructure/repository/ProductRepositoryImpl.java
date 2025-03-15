package com.owlexpress.product.infrastructure.repository;

import com.owlexpress.product.domain.entity.Product;
import com.owlexpress.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepository{
    private final ProductJpaRepository productJpaRepository;
    private final ProductQueryDslRepository productQueryDslRepository;

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        return productJpaRepository.findById(productId);
    }

    @Override
    public Page<Product> searchProducts(String q, String sort, String orderBy, PageRequest pageRequest) {
        return productQueryDslRepository.searchProducts(q, sort, orderBy,pageRequest);
    }

    @Override
    public Optional<Product> findByProductName(String productName) {
        return productJpaRepository.findByProductName(productName);
    }
}
