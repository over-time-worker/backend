package com.owlexpress.product.infrastructure.repository;

import com.owlexpress.product.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<Product, UUID> {
}
