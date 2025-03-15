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

        //상품 중복검사
        validateProductName(createProductRequestDto);

        Product product = CreateProductRequestDto.toEntity(createProductRequestDto);

        //TODO :: AuditAware 추가 후 제거
        product.updateCreateData(1L);


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
        product.setProducerId(updateProductDto.getProducerId());
        product.setProducerName(updateProductDto.getProducerName());
        product.setProducerAddress(updateProductDto.getProducerAddress());

        //TODO :: AuditAware 추가 후 제거
        product.updateModifiedData(1L);

        //TODO :: 제품 정보 수정 이벤트 전파(FeignClient)
        // 변경된 상품 정보를 연결된 허브로 FeignClient 요청을 날려서 이 제품을 포함한 허브 수정 API 발동!
        // (업데이트 시키는 동안 유저가 조회하지 못하게 막아야함)
    }


    private Product getProduct(UUID productsId) {
        return productRepository.findById(productsId).orElseThrow(
                () -> new IllegalArgumentException("찾는 회원이 없습니다.")
        );
    }

    private void validateProductName(CreateProductRequestDto createProductRequestDto) {
        productRepository.findByProductName(createProductRequestDto.getProductName()).ifPresent(
                product -> {
                    throw new IllegalArgumentException("Product already exists");
                }
        );
    }
}
