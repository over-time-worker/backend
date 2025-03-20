package com.owlexpress.deliverymanager.application.usecase;

import com.owlexpress.deliverymanager.common.exception.ExceptionMessage;
import com.owlexpress.deliverymanager.common.exception.HubDeliveryManagerException;
import com.owlexpress.deliverymanager.domain.entity.HubDeliveryManager;
import com.owlexpress.deliverymanager.domain.repository.HubDeliveryManagerRepository;
import com.owlexpress.deliverymanager.infrastructure.config.DeliveryManagerSearchConfig;
import com.owlexpress.deliverymanager.presentation.dto.request.CreateHubDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.request.UpdateHubDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.response.FindHubDeliveryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.owlexpress.deliverymanager.common.exception.ExceptionMessage.Manager_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class HubDeliveryManagerUsecase {

    private final HubDeliveryManagerRepository hubDeliveryManagerRepository;
    private final DeliveryManagerSearchConfig deliveryManagerSearchConfig;

    @Transactional
    public void create(CreateHubDeliveryManagerRequestDto createConsumerDeliveryManagerRequestDto) {
        // 가장 높은 assignNumber 가져오기
        int lastAssignNumber = hubDeliveryManagerRepository
                .findFirstByOrderByAssignNumberDesc()
                .map(HubDeliveryManager::getAssignNumber)  // 가장 큰 assignNumber 조회
                .orElse(0);  // 값이 없으면 0

        // 새로운 assignNumber 설정
        int newAssignNumber = lastAssignNumber + 1;
        HubDeliveryManager hubDeliveryManager = createConsumerDeliveryManagerRequestDto.toEntity(
                createConsumerDeliveryManagerRequestDto,newAssignNumber);

        hubDeliveryManagerRepository.save(hubDeliveryManager);

    }

    @Transactional
    public void update(
            UpdateHubDeliveryManagerRequestDto updateHubDeliveryManagerRequestDto,
            UUID hubDeliveryManagerId
    ) throws HubDeliveryManagerException.HubDuplicateAssignNumber {
        HubDeliveryManager hubDeliveryManager = getHubDeliveryManager(hubDeliveryManagerId);
        if(!hubDeliveryManagerRepository.isExistAssign_number(updateHubDeliveryManagerRequestDto.getAssign_number())){
            updateHubDeliveryManagerRequestDto.update(hubDeliveryManager);
        }else{
            throw new HubDeliveryManagerException.HubDuplicateAssignNumber(ExceptionMessage.DUPLICATE_ASSIGN_NUMBER);
        }
    }

    @Transactional(readOnly = true)
    public FindHubDeliveryResponseDto find(UUID consumerDeliveryManagerId) {
        HubDeliveryManager hubDeliveryManager = getHubDeliveryManager(consumerDeliveryManagerId);

        return FindHubDeliveryResponseDto.fromEntity(hubDeliveryManager);

    }

    @Transactional(readOnly = true)
    public PagedModel<FindHubDeliveryResponseDto> search(
            Integer page,
            Integer size,
            String sort,
            String q,
            String orderBy
    ) {
        // 페이지 크기 제한 적용
        if (!deliveryManagerSearchConfig.getAllowedPageSizes()
                                        .contains(size)) {
            size = deliveryManagerSearchConfig.getDefaultPageSize();
        }

        // 정렬 기준 제한 적용
        if (!deliveryManagerSearchConfig.getAllowedSorts()
                                        .contains(sort)) {
            sort = deliveryManagerSearchConfig.getDefaultSort();
        }

        Sort.Direction direction = orderBy.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.by(
                        direction,
                        sort
                )
        );

        Page<HubDeliveryManager> producers = hubDeliveryManagerRepository.searchProducer(
                sort,
                q,
                orderBy,
                pageRequest
        );

        Page<FindHubDeliveryResponseDto> responseDtoList = producers.map(FindHubDeliveryResponseDto::fromEntity);

        return new PagedModel<>(responseDtoList);
    }

    @Transactional
    public void delete(UUID consumerDeliveryManagerId) throws HubDeliveryManagerException.HubIsNotAvailableStatusException {
        HubDeliveryManager hubDeliveryManager = getHubDeliveryManager(consumerDeliveryManagerId);
        if (!hubDeliveryManager.getIsAvaliable()) {
            throw new HubDeliveryManagerException.HubIsNotAvailableStatusException(ExceptionMessage.IS_NOT_AVAILABLE);
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
