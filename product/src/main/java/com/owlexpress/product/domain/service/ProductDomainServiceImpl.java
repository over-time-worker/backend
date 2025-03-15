package com.owlexpress.product.domain.service;

import com.owlexpress.product.domain.entity.Product;
import com.owlexpress.product.domain.repository.ProductRepository;
import com.owlexpress.product.presentation.dto.request.CreateProductRequestDto;
import com.owlexpress.product.presentation.dto.request.UpdateProductDto;
import com.owlexpress.product.presentation.dto.response.FindProductResponse;
import com.owlexpress.product.presentation.dto.response.SearchProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
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

    @Override
    public FindProductResponse find(UUID productsId) {
        Product product = getProduct(productsId);

        AtomicInteger totalQuantity = new AtomicInteger(1000);

        product.getHubInfo().forEach(
                hubInfo ->
                        //TODO :: Hub에 재고조회 API 날려서 각 재고 모아오기
                        totalQuantity.addAndGet(hubInfo.getHubProductQuantity())
        );


        return FindProductResponse.toDTO(product,totalQuantity);
    }

    @Override
    public PagedModel<SearchProductResponseDto> search(int page, int size, String sort, String q, String orderBy) {
        Sort.Direction direction = orderBy.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sort));

        Page<Product> productPage = productRepository.searchProducts(q, sort, orderBy, pageRequest);

        Page<SearchProductResponseDto> dtoPage = productPage.map(product -> {
            AtomicInteger totalQuantity = new AtomicInteger(0);

            // hubInfo에서 모든 허브의 제품 수량을 합산
            product.getHubInfo().forEach(hubInfo -> totalQuantity.addAndGet(hubInfo.getHubProductQuantity()));

            return SearchProductResponseDto.toDTO(product, totalQuantity);
        });

        // dtoPage를 PagedModel로 변환
        return new PagedModel<>(dtoPage);
    }

    private Product getProduct(UUID productsId) {
        return productRepository.findById(productsId).orElseThrow(
                () -> new IllegalArgumentException("찾는 회원이 없습니다.")
        );
    }
}
