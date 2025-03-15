package com.owlexpress.product.infrastructure.repository;

import com.owlexpress.product.domain.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ProductQueryDslRepository {
    Page<Product> searchProducts(String q, String sort, String orderBy, PageRequest pageRequest);
}
