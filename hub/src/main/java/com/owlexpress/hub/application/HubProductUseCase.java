package com.owlexpress.hub.application;

import com.owlexpress.hub.common.HubHelper;
import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.entity.HubProduct;
import com.owlexpress.hub.domain.repository.HubRepository;
import com.owlexpress.hub.infrastructure.client.ProductClient;
import com.owlexpress.hub.presentation.dto.request.HubProductCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class HubProductUseCase {

    private final HubRepository hubRepository;
    private final ProductClient productClient;

    public HubProduct create(HubProductCreateRequestDto requestDto) {

        Hub hub = HubHelper.findByHubId(requestDto.getHubId(), hubRepository);

        // TODO: 상품 있는지 체크

        HubProduct hubProduct = requestDto.toEntityWithHub(hub);
        // TODO: PASSPORT에서 값 뺴오기
        hubProduct.createdEntity(1L);
        hub.getHubProduct().add(hubProduct);
        return hubProduct;
    }

}
