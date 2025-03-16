package com.owl_express.ai.infrastructure;

import com.owl_express.ai.application.dtos.response.MessageFindResponseDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface AiQueryRepository {
    Page<MessageFindResponseDto> searchMessages(Pageable pageable, UUID keyword);
}
