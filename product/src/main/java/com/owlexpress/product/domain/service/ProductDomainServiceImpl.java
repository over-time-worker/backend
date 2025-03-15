package com.owlexpress.product.domain.service;

import com.owlexpress.product.domain.entity.Product;
import com.owlexpress.product.domain.repository.ProductRepository;
import com.owlexpress.product.presentation.dto.request.CreateProductRequestDto;
import com.owlexpress.product.presentation.dto.request.UpdateProductDto;
import com.owlexpress.product.presentation.dto.response.FindProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

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

        //TODO :: 변경된 상품 정보를 연결된 각 허브로 FeignClient 요청을 날려서 허브 수정 API 발동!
        // (업데이트 시키는 동안 유저가 조회하지 못하게 막아야함)
    }

    @Override
    public FindProductResponse find(UUID productsId) {
        Product product = getProduct(productsId);

        AtomicInteger totalQuantity = new AtomicInteger(1000);

        product.getHubInfo().stream().forEach(
                hubInfo ->
                        totalQuantity.addAndGet(hubInfo.getHubProductQuantity()) //TODO :: Hub에 재고조회 API 날려서 각 재고 모아오기
        );


        return FindProductResponse.toDTO(product,totalQuantity);
    }

    private Product getProduct(UUID productsId) {
        return productRepository.findById(productsId).orElseThrow(
                () -> new IllegalArgumentException("찾는 회원이 없습니다.")
        );
    }
}
