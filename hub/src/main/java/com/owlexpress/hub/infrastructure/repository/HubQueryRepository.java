package com.owlexpress.hub.infrastructure.repository;

import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.entity.HubProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubQueryRepository {

    Page<Hub> searchHub(Pageable pageable, String keyword, String orderBy, String sort);

    Page<HubProduct> searchHubProduct(Pageable pageable, String keyword, String orderBy, String sort);
}
