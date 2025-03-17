package com.owlexpress.hub.infrastructure;

import com.owlexpress.hub.domain.entity.Hub;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HubJpaRepository extends JpaRepository<Hub, UUID> {

}
