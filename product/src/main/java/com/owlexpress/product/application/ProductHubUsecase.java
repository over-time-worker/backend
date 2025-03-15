package com.owlexpress.product.application;

import com.owlexpress.product.domain.entity.Product;
import com.owlexpress.product.domain.repository.ProductRepository;
import com.owlexpress.product.application.dto.response.FindProductResponse;
import com.owlexpress.product.application.dto.response.SearchProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;


@Service
@RequiredArgsConstructor
public class ProductHubUsecase {

    private final ProductRepository productRepository;

    public FindProductResponse find(UUID productsId) {
        Product product = getProduct(productsId);

        AtomicInteger totalQuantity = new AtomicInteger(1000);

        product.getHubInfo().forEach(
                hubInfo ->
                        //TODO :: Hub에 재고조회 API 날려서 각 재고 모아오기
                        totalQuantity.addAndGet(hubInfo.getHubProductQuantity())
        );
        return FindProductResponse.toDTO(product, totalQuantity);
    }

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
