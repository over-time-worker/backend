package com.owl_express.ai.infrastructure;

import com.owl_express.ai.domain.entity.Ai;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiJpaRepository extends JpaRepository<Ai, UUID> {

}
