package com.owlexpress.hub.infrastructure.repository.hub;

import com.owlexpress.hub.application.dto.response.HubProductInfoResponseDto;
import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.entity.HubProduct;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubQueryRepository {

    Page<Hub> searchHub(Pageable pageable, String keyword, String orderBy, String sort);

    Page<HubProduct> searchHubProduct(Pageable pageable, String keyword, String orderBy, String sort);

    List<HubProductInfoResponseDto> test(List<UUID> hubProductIds);
}
