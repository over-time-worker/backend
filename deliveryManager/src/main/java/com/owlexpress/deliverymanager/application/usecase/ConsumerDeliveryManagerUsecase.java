package com.owlexpress.deliverymanager.application.usecase;

import com.owlexpress.deliverymanager.presentation.dto.request.UpdateConsumerDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.response.FindConsumerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsumerDeliveryManagerUsecase {
    public void create(ConsumerDeliveryManagerUsecase consumerDeliveryManagerUsecase) {
    }

    public void update(
            UpdateConsumerDeliveryManagerRequestDto updateConsumerDeliveryManagerRequestDto,
            UUID consumerDeliveryManagerId
    ) {
    }

    public FindConsumerResponseDto find(UUID consumerDeliveryManagerId) {
        return null;
    }

    public PagedModel<FindConsumerResponseDto> search(
            Integer page,
            Integer size,
            String sort,
            String q,
            String orderBy
    ) {
        return null;
    }

    public void delete(UUID consumerDeliveryManagerId) {

    }
}
