package com.owlexpress.hub.infrastructure.repository;

import com.owlexpress.hub.domain.entity.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HubQueryRepository {

    Page<Hub> searchHub(Pageable pageable, String keyword, String orderBy, String sort);

}
