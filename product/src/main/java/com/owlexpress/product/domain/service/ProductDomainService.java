package com.owlexpress.product.domain.service;

import com.owlexpress.product.presentation.dto.request.CreateProductRequestDto;
import com.owlexpress.product.presentation.dto.request.UpdateProductDto;
import com.owlexpress.product.presentation.dto.response.FindProductResponse;
import com.owlexpress.product.presentation.dto.response.SearchProductResponseDto;
import org.springframework.data.web.PagedModel;

import java.util.UUID;

public interface ProductDomainService {
    void createProduct(CreateProductRequestDto mockingData);

    void updateProduct(UpdateProductDto mockingData, UUID productsId);

    FindProductResponse find(UUID productsId);

    PagedModel<SearchProductResponseDto> search(int page, int size, String sort, String q, String orderBy);
}
