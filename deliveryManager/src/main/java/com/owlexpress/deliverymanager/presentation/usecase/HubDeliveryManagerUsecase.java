package com.owlexpress.deliverymanager.presentation.usecase;

import com.owlexpress.deliverymanager.common.dto.request.CreateHubDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.common.dto.request.DeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.common.dto.request.UpdateHubDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.common.dto.response.AlarmCreateResponseDto;
import com.owlexpress.deliverymanager.common.dto.response.FindHubDeliveryResponseDto;
import com.owlexpress.deliverymanager.common.exception.HubDeliveryManagerException;
import com.owlexpress.deliverymanager.common.exception.HubDeliveryManagerException.HubDuplicateAssignNumber;
import java.util.UUID;
import org.springframework.data.web.PagedModel;

public interface HubDeliveryManagerUsecase {

    void create(CreateHubDeliveryManagerRequestDto createConsumerDeliveryManagerRequestDto, String passport);

    void update(UpdateHubDeliveryManagerRequestDto updateHubDeliveryManagerRequestDto, UUID hubDeliveryManagerId, String passport) throws HubDuplicateAssignNumber;

    FindHubDeliveryResponseDto find(UUID consumerDeliveryManagerId);

    PagedModel<FindHubDeliveryResponseDto> search(Integer page, Integer size, String sort, String q, String orderBy);

    void delete(UUID consumerDeliveryManagerId, String passport) throws HubDeliveryManagerException.HubIsNotAvailableStatusException;

    AlarmCreateResponseDto assign(DeliveryManagerRequestDto deliveryManagerRequestDto, String passport);

    void returnHub(UUID deliveryManagerId);
}
