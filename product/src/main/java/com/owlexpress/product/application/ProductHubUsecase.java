package com.owlexpress.product.application;

import com.owlexpress.product.application.dto.response.FindProductResponseDto;
import com.owlexpress.product.application.dto.response.SearchProductResponseDto;
import com.owlexpress.product.common.exceptions.ProductException;
import com.owlexpress.product.domain.entity.HubInfo;
import com.owlexpress.product.domain.entity.Product;
import com.owlexpress.product.domain.repository.ProductRepository;
import com.owlexpress.product.infrastructure.config.ProductSearchConfig;
import com.owlexpress.product.presentation.dto.request.CreateHubInfoRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ProductHubUsecase {

    private final ProductRepository productRepository;
    private final ProductSearchConfig productSearchConfig; // 설정 클래스 주입


    public FindProductResponseDto find(UUID productsId) {
        Product product = getProduct(productsId);

        AtomicInteger totalQuantity = new AtomicInteger(1000);

        product.getHubInfo().forEach(
                hubInfo ->
                        //TODO :: Hub에 재고조회 API 날려서 각 재고 모아오기
                        totalQuantity.addAndGet(hubInfo.getHubProductQuantity())
        );
        return FindProductResponseDto.toDTO(product, totalQuantity);
    }

    public PagedModel<SearchProductResponseDto> search(
            int page,
            int size,
            String sort,
            String q,
            String orderBy
    )
    {
        // 페이지 크기 제한 적용
        if (!productSearchConfig.getAllowedPageSizes().contains(size)) {
            size = productSearchConfig.getDefaultPageSize();
        }

        // 정렬 기준 제한 적용
        if (!productSearchConfig.getAllowedSorts().contains(sort)) {
            sort = productSearchConfig.getDefaultSort();
        }

        Sort.Direction direction = orderBy.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sort));

        Page<Product> productPage = productRepository.searchProducts(q, sort, orderBy, pageRequest
        );

        Page<SearchProductResponseDto> dtoPage = productPage.map(product -> {
            AtomicInteger totalQuantity = new AtomicInteger(0);

            // hubInfo에서 모든 허브의 제품 수량을 합산
            product.getHubInfo().forEach(hubInfo -> totalQuantity.addAndGet(hubInfo.getHubProductQuantity()));

            return SearchProductResponseDto.toDTO(product, totalQuantity);
        });

        return new PagedModel<>(dtoPage);
    }

    @Transactional
    public void connect(
            CreateHubInfoRequestDto createHubInfoRequestDto,
            HubInfo hubInfo
    )
    {
        Product product = getProduct(createHubInfoRequestDto.getProductId());
        product.getHubInfo().add(hubInfo);
        product  = productRepository.save(product);
        log.info(product.getHubInfo().toString());
        product.updateModifiedData(1L); //TODO :: AUdit설정후 삭제

        hubInfo.setProduct(product);
    }

    private Product getProduct(UUID productsId) {
        log.info("productsId: {}", productsId);
        return productRepository.findById(productsId).orElseThrow(
                () -> new ProductException.ProductNotFoundException("찾는 상품이 없습니다")
        );
    }

    @Transactional
    public void disConnect(HubInfo hubInfo) {
        Product product = hubInfo.getProduct();
        product.getHubInfo().remove(hubInfo);
        hubInfo.setProduct(null);
        productRepository.save(product);
    }
}
