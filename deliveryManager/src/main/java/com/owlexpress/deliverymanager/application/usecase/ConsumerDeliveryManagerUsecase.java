package com.owlexpress.deliverymanager.application.usecase;

import com.owlexpress.deliverymanager.common.exception.ConsumerDeliveryManagerException;
import com.owlexpress.deliverymanager.common.exception.ExceptionMessage;
import com.owlexpress.deliverymanager.domain.entity.ConsumerDeliveryManager;
import com.owlexpress.deliverymanager.domain.repository.ConsumerDeliveryManagerRepository;
import com.owlexpress.deliverymanager.infrastructure.config.DeliveryManagerSearchConfig;
import com.owlexpress.deliverymanager.infrastructure.feignClient.HubClient;
import com.owlexpress.deliverymanager.presentation.dto.request.CreateConsumerDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.request.UpdateConsumerDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.response.FindConsumerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.owlexpress.deliverymanager.common.exception.ExceptionMessage.Manager_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class ConsumerDeliveryManagerUsecase {
    private final ConsumerDeliveryManagerRepository consumerDeliveryManagerRepository;
    private final DeliveryManagerSearchConfig deliveryManagerSearchConfig;
    private final HubClient hubClient;

    @Transactional
    public void create(CreateConsumerDeliveryManagerRequestDto consumerDeliveryManagerUsecase) {
        ConsumerDeliveryManager consumerDeliveryManager = consumerDeliveryManagerUsecase.toEntity(
                consumerDeliveryManagerUsecase);
        consumerDeliveryManagerRepository.save(consumerDeliveryManager);

    }


    @Transactional
    public void update(
            UpdateConsumerDeliveryManagerRequestDto updateConsumerDeliveryManagerRequestDto,
            UUID consumerDeliveryManagerId
    ) throws ConsumerDeliveryManagerException.ConsumerDuplicateAssignNumberException {
        ConsumerDeliveryManager consumerDeliveryManager = getConsumerDeliveryManager(consumerDeliveryManagerId);

        //이미 존재하는 번호이면 예외 발생
        if (validateAssignNumber(updateConsumerDeliveryManagerRequestDto)) {
            throw new ConsumerDeliveryManagerException.ConsumerDuplicateAssignNumberException(
                    ExceptionMessage.DUPLICATE_ASSIGN_NUMBER);
            //기존 할당 번호와 다르다면 수정
        } else if (!Objects.equals(consumerDeliveryManager.getAssignNumber(),
                                   updateConsumerDeliveryManagerRequestDto.getAssignNumber()
        ) && validateAssignNumber(updateConsumerDeliveryManagerRequestDto)) {
            updateConsumerDeliveryManagerRequestDto.setAssignNumber(
                    updateConsumerDeliveryManagerRequestDto.getAssignNumber());
        }

        //허브가 존재하는지 클라이언트 통신후 정상이면 허브 변경
        Optional.of(hubClient.find(consumerDeliveryManagerId))
                .ifPresent(hubFindResponseDtoCommonDto -> consumerDeliveryManager.setHubId(
                        hubFindResponseDtoCommonDto.getData()
                                                   .getHubId()));
    }

    @Transactional(readOnly = true)
    public FindConsumerResponseDto find(UUID consumerDeliveryManagerId) {
        ConsumerDeliveryManager consumerDeliveryManager = getConsumerDeliveryManager(consumerDeliveryManagerId);
        return FindConsumerResponseDto.fromEntity(consumerDeliveryManager);
    }

    @Transactional(readOnly = true)
    public PagedModel<FindConsumerResponseDto> search(
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
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sort));


        Page<ConsumerDeliveryManager> producers = consumerDeliveryManagerRepository.searchProducer(sort, q, orderBy,
                                                                                                   pageRequest
        );

        Page<FindConsumerResponseDto> responseDtoList = producers.map(FindConsumerResponseDto::fromEntity);

        return new PagedModel<>(responseDtoList);
    }

    @Transactional
    public void delete(UUID consumerDeliveryManagerId) throws ConsumerDeliveryManagerException.ConsumerDeliveryManagerNotAvailableException {
        ConsumerDeliveryManager consumerDeliveryManager = getConsumerDeliveryManager(consumerDeliveryManagerId);

        if (!consumerDeliveryManager.getIsAvaliable()) {
            throw new ConsumerDeliveryManagerException.ConsumerDeliveryManagerNotAvailableException(
                    ExceptionMessage.IS_NOT_AVAILABLE);
        }
        consumerDeliveryManager.softDeleteData(1L);
    }

    private boolean validateAssignNumber(UpdateConsumerDeliveryManagerRequestDto updateConsumerDeliveryManagerRequestDto) {
        return !consumerDeliveryManagerRepository.existByAssignNumber(
                updateConsumerDeliveryManagerRequestDto.getAssignNumber());
    }

    private ConsumerDeliveryManager getConsumerDeliveryManager(UUID consumerDeliveryManagerId) {
        return consumerDeliveryManagerRepository.findById(consumerDeliveryManagerId)
                                                .orElseThrow(
                                                        () -> new ConsumerDeliveryManagerException.ConsumerDeliveryManagerNotFoundException(
                                                                Manager_NOT_FOUND_MESSAGE));
    }
}
