package com.owlexpress.producer.domain.service;


import com.owlexpress.producer.presentation.dto.request.CreateProductInfoRequestDto;

public interface ProductInfoService {
    void create(CreateProductInfoRequestDto createProductInfoRequestDto);
}
