package com.owl_express.ai.domain.repository;

import com.owl_express.ai.domain.entity.Ai;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface AiRepository {
    Ai save(Ai ai);
    Optional<Ai> findById(UUID id);
}
