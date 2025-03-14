package com.owl_express.ai.domain.repository;

import com.owl_express.ai.domain.entity.Ai;
import org.springframework.stereotype.Repository;

@Repository
public interface AiRepository {
    Ai save(Ai ai);
}
