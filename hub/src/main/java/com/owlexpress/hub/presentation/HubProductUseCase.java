package com.owlexpress.hub.presentation;

import com.owlexpress.hub.domain.entity.HubProduct;
import com.owlexpress.hub.presentation.dto.request.HubProductCreateRequestDto;
import com.owlexpress.hub.presentation.dto.request.OrderConfirmRequestDto;
import com.owlexpress.hub.presentation.dto.response.HubProductOrderConfirmResponseDto;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public interface HubProductUseCase {

    HubProduct create(
            HubProductCreateRequestDto requestDto,
            String passport
    );

    @Transactional
    void delete(
            UUID hubProductId,
            String passport
    );

    HubProductOrderConfirmResponseDto confirmOrder(
            OrderConfirmRequestDto requestDto
    );

    void rollbackConfirmOrder(UUID orderId);
}
