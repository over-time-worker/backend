package com.owlexpress.deliverymanager.application.usecase;

import com.owlexpress.deliverymanager.common.exception.ExceptionMessage;
import com.owlexpress.deliverymanager.common.exception.HubDeliveryManagerException;
import com.owlexpress.deliverymanager.domain.entity.HubDeliveryManager;
import com.owlexpress.deliverymanager.domain.repository.HubDeliveryManagerRepository;
import com.owlexpress.deliverymanager.presentation.dto.request.CreateHubDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.request.UpdateHubDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.response.FindHubDeliveryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.owlexpress.deliverymanager.common.exception.ExceptionMessage.Manager_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class HubDeliveryManagerUsecase {

    private final HubDeliveryManagerRepository hubDeliveryManagerRepository;

    @Transactional
    public void create(CreateHubDeliveryManagerRequestDto createConsumerDeliveryManagerRequestDto) {
        HubDeliveryManager hubDeliveryManager = createConsumerDeliveryManagerRequestDto.toEntity(
                createConsumerDeliveryManagerRequestDto);

        hubDeliveryManagerRepository.save(hubDeliveryManager);

    }

    @Transactional
    public void update(
            UpdateHubDeliveryManagerRequestDto updateHubDeliveryManagerRequestDto,
            UUID hubDeliveryManagerId
    ) throws HubDeliveryManagerException.DuplicateAssignNumber {
        HubDeliveryManager hubDeliveryManager = getHubDeliveryManager(hubDeliveryManagerId);
        if(!hubDeliveryManagerRepository.isExistAssign_number(updateHubDeliveryManagerRequestDto.getAssign_number())){
            updateHubDeliveryManagerRequestDto.update(hubDeliveryManager);
        }else{
            throw new HubDeliveryManagerException.DuplicateAssignNumber(ExceptionMessage.DUPLICATE_ASSIGN_NUMBER);
        }
    }

    public FindHubDeliveryResponseDto find(UUID consumerDeliveryManagerId) {
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

    public void delete(UUID consumerDeliveryManagerId) throws HubDeliveryManagerException.IsNotAvailableStatusException {
        HubDeliveryManager hubDeliveryManager = getHubDeliveryManager(consumerDeliveryManagerId);
        if (!hubDeliveryManager.getIsAvaliable()) {
            throw new HubDeliveryManagerException.IsNotAvailableStatusException(ExceptionMessage.IS_NOT_AVAILABLE);
        }
        hubDeliveryManager.softDeleteData(1L);
    }

    private HubDeliveryManager getHubDeliveryManager(UUID hubDeliveryManagerId) {
        return hubDeliveryManagerRepository.findById(hubDeliveryManagerId)
                                           .orElseThrow(
                                                   () -> new HubDeliveryManagerException.HubDeliveryManagerNotFoundException(
                                                           Manager_NOT_FOUND_MESSAGE));
    }
}
