package com.owlexpress.product.domain.repository;

import com.owlexpress.product.domain.entity.Product;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);

    Optional<Product> findById(UUID productsId);

    Page<Product> searchProducts(String q, String sort, String orderBy, PageRequest pageRequest);

    Optional<Product> findByProductName(@Size(min = 1, max = 50, message = "[size:productName]") String productName);
}
