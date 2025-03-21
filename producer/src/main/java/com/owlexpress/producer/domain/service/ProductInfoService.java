package com.owlexpress.producer.domain.service;


import com.owlexpress.producer.presentation.dto.request.CreateProductInfoRequestDto;
import com.owlexpress.producer.presentation.dto.request.UpdateProductInfoRequestDto;

import java.util.UUID;

public interface ProductInfoService {
    void create(CreateProductInfoRequestDto createProductInfoRequestDto,
                String passport
    );

    void update(
            UUID productId,
            UpdateProductInfoRequestDto updateProductInfoRequestDto,
            String passport
    );

    void delete(UUID productId,
                String passport
    );
}
