package com.owl_express.ai.infrastructure;

import com.owl_express.ai.domain.entity.Ai;
import com.owl_express.ai.domain.repository.AiRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

    @Override
    public Optional<Ai> findById(UUID id) {
        return aiJpaRepository.findById(id);
    }
}
