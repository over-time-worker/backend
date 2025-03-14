package com.owl_express.ai.infrastructure;

import com.owl_express.ai.domain.entity.Ai;
import com.owl_express.ai.domain.repository.AiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AiRepositoryImpl implements AiRepository {
    private final AiJpaRepository aiJpaRepository;
    private final AiQueryRepository aiQueryRepository;

    @Override
    public Ai save(Ai ai) {
        return aiJpaRepository.save(ai);
    }
}
