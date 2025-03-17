package com.owlexpress.producer.domain.service;


import com.owlexpress.producer.presentation.dto.request.CreateProductInfoRequestDto;
import com.owlexpress.producer.presentation.dto.request.UpdateProductInfoRequestDto;
import jakarta.validation.Valid;

import java.util.UUID;

public interface ProductInfoService {
    void create(CreateProductInfoRequestDto createProductInfoRequestDto);

    void update(
            UUID productId,
            @Valid UpdateProductInfoRequestDto updateProductInfoRequestDto
    );

    void delete(UUID productId);
}
