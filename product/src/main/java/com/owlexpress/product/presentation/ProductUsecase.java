package com.owlexpress.product.presentation;

import com.owlexpress.product.application.dto.response.FindProductResponseDto;
import com.owlexpress.product.application.dto.response.SearchProductResponseDto;
import com.owlexpress.product.common.exceptions.ProductException;
import com.owlexpress.product.domain.entity.HubInfo;
import com.owlexpress.product.presentation.dto.request.CreateHubInfoRequestDto;
import com.owlexpress.product.presentation.dto.request.CreateProductRequestDto;
import com.owlexpress.product.presentation.dto.request.UpdateProductDto;
import java.util.UUID;
import org.springframework.data.web.PagedModel;
import org.springframework.transaction.annotation.Transactional;

public interface ProductUsecase {

    @Transactional
    void createProduct(
            CreateProductRequestDto createProductRequestDto,
            String passport
    ) throws ProductException.ProductCreateFailException;

    @Transactional
    void updateProduct(
            UpdateProductDto updateProductDto,
            UUID productsId,
            String passport
    ) throws ProductException.ProductUpdateFailException;

    @Transactional(readOnly = true)
    FindProductResponseDto find(UUID productsId);

    @Transactional
    void delete(UUID productsId,
            String passport
    );

    @Transactional(readOnly = true)
    PagedModel<SearchProductResponseDto> search(
            int page,
            int size,
            String sort,
            String q,
            String orderBy
    );

    @Transactional
    void connect(
            CreateHubInfoRequestDto createHubInfoRequestDto,
            HubInfo hubInfo
    );

    @Transactional
    void disConnect(HubInfo hubInfo);
}
