package com.owlexpress.product.domain.service;

import com.owlexpress.product.domain.entity.Product;
import com.owlexpress.product.domain.repository.ProductRepository;
import com.owlexpress.product.presentation.dto.request.CreateProductRequestDto;
import com.owlexpress.product.presentation.dto.request.UpdateProductDto;
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

        //TODO:: 상품명 중복 검사

        productRepository.save(product);
    }

    @Override
    @Transactional
    public void updateProduct(UpdateProductDto updateProductDto, UUID productsId) {
        //제품 정보 수정
        Product product = getProduct(productsId);
        product.setProductName(updateProductDto.getProductName());
        product.setProductPrice(updateProductDto.getProductPrice());
        product.setProductType(updateProductDto.getProductType());

        //TODO :: 제품 정보 수정 이벤트 전파(FeignClient)
        // 변경된 상품 정보를 연결된 허브로 FeignClient 요청을 날려서 이 제품을 포함한 허브 수정 API 발동!
        // (업데이트 시키는 동안 유저가 조회하지 못하게 막아야함)
    }


    private Product getProduct(UUID productsId) {
        return productRepository.findById(productsId).orElseThrow(
                () -> new IllegalArgumentException("찾는 회원이 없습니다.")
        );
    }
}
