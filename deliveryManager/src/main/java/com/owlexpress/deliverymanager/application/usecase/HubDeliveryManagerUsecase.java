package com.owlexpress.deliverymanager.application.usecase;

import com.owlexpress.deliverymanager.presentation.dto.request.CreateConsumerDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.request.UpdateHubDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.response.FindHubDeliveryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubDeliveryManagerUsecase {
    public void create(CreateConsumerDeliveryManagerRequestDto createConsumerDeliveryManagerRequestDto) {

    }

    public void update(UpdateHubDeliveryManagerRequestDto updateHubDeliveryManagerRequestDto) {
    }

    public FindHubDeliveryResponseDto get(UUID consumerDeliveryManagerId) {
        return null;
    }

    public PagedModel search(
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
