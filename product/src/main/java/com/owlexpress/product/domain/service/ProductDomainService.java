package com.owlexpress.product.domain.service;

import com.owlexpress.product.presentation.dto.request.CreateProductRequestDto;
import com.owlexpress.product.presentation.dto.request.UpdateProductDto;

import java.util.UUID;

public interface ProductDomainService {
    void createProduct(CreateProductRequestDto mockingData);

    void updateProduct(UpdateProductDto mockingData, UUID productsId);

    void delete(UUID productsId);
}
