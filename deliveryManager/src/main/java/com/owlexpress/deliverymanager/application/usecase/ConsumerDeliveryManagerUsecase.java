package com.owlexpress.deliverymanager.application.usecase;

import com.owlexpress.deliverymanager.application.dto.response.AlarmCreateResponseDto;
import com.owlexpress.deliverymanager.common.exception.ConsumerDeliveryManagerException;
import com.owlexpress.deliverymanager.common.exception.ExceptionMessage;
import com.owlexpress.deliverymanager.domain.dto.response.HubFindResponseDto;
import com.owlexpress.deliverymanager.domain.entity.ConsumerDeliveryManager;
import com.owlexpress.deliverymanager.domain.repository.ConsumerDeliveryManagerRepository;
import com.owlexpress.deliverymanager.infrastructure.CommonDto;
import com.owlexpress.deliverymanager.infrastructure.config.DeliveryManagerSearchConfig;
import com.owlexpress.deliverymanager.infrastructure.feignClient.HubClient;
import com.owlexpress.deliverymanager.presentation.dto.request.CreateConsumerDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.request.DeliveryManagerRequestDto;
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

import static com.owlexpress.deliverymanager.common.exception.ConsumerDeliveryManagerException.*;
import static com.owlexpress.deliverymanager.common.exception.ExceptionMessage.Manager_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class ConsumerDeliveryManagerUsecase {
    private final ConsumerDeliveryManagerRepository consumerDeliveryManagerRepository;
    private final DeliveryManagerSearchConfig deliveryManagerSearchConfig;
    private final HubClient hubClient;

    @Transactional
    public void create(CreateConsumerDeliveryManagerRequestDto consumerDeliveryManagerUsecase) throws HubNotFoundException {

        // 허브가 존재하는지 확인 (존재하지 않으면 예외 발생)
        boolean hubExists = Optional.ofNullable(hubClient.find(consumerDeliveryManagerUsecase.getHubId()))
                                    .isPresent();
        if (!hubExists) {
            throw new HubNotFoundException(ExceptionMessage.HUB_NOT_FOUND);
        }

        int lastAssignNumber = consumerDeliveryManagerRepository.findFirstByOrderByAssignNumberDesc()
                                                                .map(ConsumerDeliveryManager::getAssignNumber)  // 가장 큰 assignNumber 조회
                                                                .orElse(0);
        int newAssignNumber = lastAssignNumber + 1;
        ConsumerDeliveryManager consumerDeliveryManager = consumerDeliveryManagerUsecase.toEntity(
                consumerDeliveryManagerUsecase, newAssignNumber);
        consumerDeliveryManagerRepository.save(consumerDeliveryManager);

    }


    @Transactional
    public void update(
            UpdateConsumerDeliveryManagerRequestDto updateConsumerDeliveryManagerRequestDto,
            UUID consumerDeliveryManagerId
    ) throws ConsumerDuplicateAssignNumberException {
        // 기존 배송 담당자 조회
        ConsumerDeliveryManager consumerDeliveryManager = getConsumerDeliveryManager(consumerDeliveryManagerId);

        // 할당 번호가 변경될 경우 중복 검사
        Integer newAssignNumber = updateConsumerDeliveryManagerRequestDto.getAssignNumber();
        if (!Objects.equals(consumerDeliveryManager.getAssignNumber(), newAssignNumber)) {
            if (validateAssignNumber(updateConsumerDeliveryManagerRequestDto)) {
                throw new ConsumerDuplicateAssignNumberException(
                        ExceptionMessage.DUPLICATE_ASSIGN_NUMBER);
            }
            // 할당 번호 업데이트
            consumerDeliveryManager.setAssignNumber(newAssignNumber);
        }

        // 허브 존재 여부 확인 (존재하지 않으면 예외 발생)
        UUID newHubId = updateConsumerDeliveryManagerRequestDto.getHubId();
        if (newHubId != null) {
            boolean hubExists = Optional.ofNullable(hubClient.find(newHubId)).isPresent();
            if (!hubExists) {
                throw new IllegalArgumentException("존재하지 않는 허브 ID입니다: " + newHubId);
            }
            consumerDeliveryManager.setHubId(newHubId);
        }
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
    public void delete(UUID consumerDeliveryManagerId) throws ConsumerDeliveryManagerNotAvailableException {
        ConsumerDeliveryManager consumerDeliveryManager = getConsumerDeliveryManager(consumerDeliveryManagerId);

        if (!consumerDeliveryManager.getIsAvaliable()) {
            throw new ConsumerDeliveryManagerNotAvailableException(
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
                                                        () -> new ConsumerDeliveryManagerNotFoundException(
                                                                Manager_NOT_FOUND_MESSAGE));
    }

    @Transactional
    public AlarmCreateResponseDto assign(DeliveryManagerRequestDto deliveryManagerRequestDto) throws HubNotFoundException, ConsumerEmptyException {
        CommonDto<HubFindResponseDto> hubFindResponseDtoCommonDto = hubClient.find(
                deliveryManagerRequestDto.getCurrentHubId());

        // 허브가 존재하는지 확인
        if (hubFindResponseDtoCommonDto == null || hubFindResponseDtoCommonDto.getData() == null) {
            throw new HubNotFoundException(ExceptionMessage.HUB_NOT_FOUND);
        }

        // 가장 낮은 assignNumber를 가진 isAvailable = true인 담당자 조회
        UUID hubId = hubFindResponseDtoCommonDto.getData()
                                                .getHubId();
        Optional<ConsumerDeliveryManager> optionalManager =
                consumerDeliveryManagerRepository.findFirstByHubIdAndIsAvaliableTrueOrderByAssignNumberAsc(hubId);

        // 담당자가 없는 경우 예외 처리 또는 기본 응답
        if (optionalManager.isEmpty()) {
            throw new ConsumerDeliveryManagerException.ConsumerEmptyException(ExceptionMessage.CONSUMER_NOT_ENOUGH+hubId);
        }
        ConsumerDeliveryManager manager = optionalManager.get();
        manager.setIsAvaliable(false);


        // DTO 변환 및 반환
        return AlarmCreateResponseDto.from(manager,deliveryManagerRequestDto);
    }

    @Transactional
    public void returnHub(UUID deliveryManagerId) {
        getConsumerDeliveryManager(deliveryManagerId).setIsAvaliable(true);
    }
}
