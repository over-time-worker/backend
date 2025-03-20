package com.owlexpress.deliverymanager.application.usecase;

import com.owlexpress.deliverymanager.common.exception.ConsumerDeliveryManagerException;
import com.owlexpress.deliverymanager.common.exception.ExceptionMessage;
import com.owlexpress.deliverymanager.domain.entity.ConsumerDeliveryManager;
import com.owlexpress.deliverymanager.domain.repository.ConsumerDeliveryManagerRepository;
import com.owlexpress.deliverymanager.infrastructure.feignClient.HubClient;
import com.owlexpress.deliverymanager.presentation.dto.request.CreateConsumerDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.request.UpdateConsumerDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.response.FindConsumerResponseDto;
import lombok.RequiredArgsConstructor;
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
        } else if (!Objects.equals(
                consumerDeliveryManager.getAssignNumber(),
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

    private boolean validateAssignNumber(UpdateConsumerDeliveryManagerRequestDto updateConsumerDeliveryManagerRequestDto) {
        return !consumerDeliveryManagerRepository.existByAssignNumber(
                updateConsumerDeliveryManagerRequestDto.getAssignNumber());
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

    public void delete(UUID consumerDeliveryManagerId) throws ConsumerDeliveryManagerException.ConsumerDeliveryManagerNotAvailableException {
        ConsumerDeliveryManager consumerDeliveryManager = getConsumerDeliveryManager(consumerDeliveryManagerId);

        if (!consumerDeliveryManager.getIsAvaliable()) {
            throw new ConsumerDeliveryManagerException.ConsumerDeliveryManagerNotAvailableException(ExceptionMessage.IS_NOT_AVAILABLE);
        }
        consumerDeliveryManager.softDeleteData(1L);
    }

    private ConsumerDeliveryManager getConsumerDeliveryManager(UUID consumerDeliveryManagerId) {
        ConsumerDeliveryManager consumerDeliveryManager = consumerDeliveryManagerRepository.findById(
                                                                                                   consumerDeliveryManagerId)
                                                                                           .orElseThrow(
                                                                                                   () -> new ConsumerDeliveryManagerException.ConsumerDeliveryManagerNotFoundException(
                                                                                                           Manager_NOT_FOUND_MESSAGE));
        return consumerDeliveryManager;
    }
}
