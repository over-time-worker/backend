package com.owlexpress.product.domain.service;

import com.owlexpress.product.domain.repository.ProductRepository;
import com.owlexpress.product.domain.entity.Product;
import com.owlexpress.product.presentation.dto.request.CreateProductRequestDto;
import com.owlexpress.product.presentation.dto.request.UpdateProductDto;
import com.owlexpress.product.presentation.dto.response.FindProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductDomainServiceImpl implements ProductDomainService {
    private final ProductRepository productRepository;

    @Override
    public void createProduct(CreateProductRequestDto createProductRequestDto) {

        Product product = Product.builder()
                .productName(createProductRequestDto.getProductName())
                .productPrice(createProductRequestDto.getProductPrice())
                .productType(createProductRequestDto.getProductType())
                .build();

        productRepository.save(product);
    }

    @Override
    @Transactional
    public void updateProduct(UpdateProductDto updateProductDto, UUID productsId) {
        Product product = getProduct(productsId);
        product.setProductName(updateProductDto.getProductName());
        product.setProductPrice(updateProductDto.getProductPrice());
        product.setProductType(updateProductDto.getProductType());
    }

    @Override
    public FindProductResponse find(UUID productsId) {
        Product product = getProduct(productsId);

        return FindProductResponse.toDTO(product);
    }

    private Product getProduct(UUID productsId) {
        return productRepository.findById(productsId).orElseThrow(
                () -> new IllegalArgumentException("찾는 회원이 없습니다.")
        );
    }
}
