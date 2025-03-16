package com.owl_express.ai.domain.repository;

import com.owl_express.ai.application.dtos.response.MessageFindResponseDto;
import com.owl_express.ai.domain.entity.Ai;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface AiRepository {
    Ai save(Ai ai);
    Optional<Ai> findById(UUID id);
    Page<MessageFindResponseDto> searchMessages(Pageable pageable, UUID keyword);
}
