package com.owlexpress.hub.infrastructure.repository.hub;

import com.owlexpress.hub.application.dto.response.HubProductInfoResponseDto;
import com.owlexpress.hub.application.dto.response.HubProductStockResponseDto;
import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.entity.HubProduct;
import com.owlexpress.hub.domain.repository.HubRepository;
import com.owlexpress.hub.presentation.dto.response.HubProductSearchResponseDto;
import com.owlexpress.hub.presentation.dto.response.HubSearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepository {

    private final HubJpaRepository hubJpaRepository;

    private final HubQueryRepository hubQueryRepository;

    @Override
    public Optional<Hub> findByHubId(UUID hubId) {
        return hubJpaRepository.findById(hubId);
    }

    @Override
    public PagedModel<HubSearchResponseDto> searchHub(Pageable pageable, String keyword,
            String orderBy, String sort) {
        Page<Hub> hubs = hubQueryRepository.searchHub(pageable, keyword, orderBy, sort);
        Page<HubSearchResponseDto> results = hubs.map(HubSearchResponseDto::fromEntity);
        return new PagedModel<>(results);
    }

    @Override
    public Hub save(Hub hub) {
        return hubJpaRepository.save(hub);
    }

    /*
    허브 상품
     */
    @Override
    public PagedModel<HubProductSearchResponseDto> searchHubProduct(Pageable pageable,
            String keyword, String orderBy, String sort) {
        Page<HubProductSearchResponseDto> products =
                hubQueryRepository.searchHubProduct(pageable, keyword, orderBy, sort)
                        .map(HubProductSearchResponseDto::fromEntity);

        return new PagedModel<>(products);
    }

    @Override
    public Optional<HubProduct> findByHubProductId(UUID hubProductId) {
        return hubJpaRepository.findByHubProductId(hubProductId);
    }

    @Override
    public List<Hub> findAllWithIntervals() {
        return hubJpaRepository.findAllWithIntervals();
    }

    @Override
    public Optional<Hub> findById(UUID startHubId) {
        return hubJpaRepository.findById(startHubId);
    }

    @Override
    public List<Hub> findAllCentralHub() {
        return hubJpaRepository.findAllByParentHubIsNull();
    }

    @Override
    public List<Hub> findAllByParentHub(Hub parentHubId) {
        return hubJpaRepository.findAllByParentHub(parentHubId);
    }

    @Override
    public List<HubProductStockResponseDto> findHubProductStocks(List<UUID> products) {
        return hubJpaRepository.findProductsWithStock(products);
    }

    @Override
    public List<HubProductInfoResponseDto> findAllHubProductsInOrders(List<UUID> productIds) {
        return hubQueryRepository.findAllHubProductsInOrders(productIds);
    }

}
