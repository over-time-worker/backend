package com.owlexpress.deliverymanager.application.usecase;

import com.owlexpress.deliverymanager.application.dto.response.AlarmCreateResponseDto;
import com.owlexpress.deliverymanager.common.dto.request.AlarmCreateRequestDto;
import com.owlexpress.deliverymanager.common.dto.response.PassportDto;
import com.owlexpress.deliverymanager.common.exception.ExceptionMessage;
import com.owlexpress.deliverymanager.common.exception.HubDeliveryManagerException;
import com.owlexpress.deliverymanager.common.exception.HubDeliveryManagerException.HubDuplicateAssignNumber;
import com.owlexpress.deliverymanager.common.helper.PassportHelper;
import com.owlexpress.deliverymanager.domain.entity.HubDeliveryManager;
import com.owlexpress.deliverymanager.domain.repository.HubDeliveryManagerRepository;
import com.owlexpress.deliverymanager.infrastructure.config.DeliveryManagerSearchConfig;
import com.owlexpress.deliverymanager.infrastructure.feignClient.AlarmClient;
import com.owlexpress.deliverymanager.presentation.dto.request.CreateHubDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.request.DeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.request.UpdateHubDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.response.FindHubDeliveryResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.owlexpress.deliverymanager.common.exception.ExceptionMessage.Manager_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubDeliveryManagerUsecase {

    private final HubDeliveryManagerRepository hubDeliveryManagerRepository;
    private final DeliveryManagerSearchConfig deliveryManagerSearchConfig;
    private final PassportHelper passportHelper;
    private final AlarmClient alarmClient;

    @Transactional
    public void create(CreateHubDeliveryManagerRequestDto createConsumerDeliveryManagerRequestDto,
                       String passport
    ) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        // 가장 높은 assignNumber 가져오기
        int lastAssignNumber = hubDeliveryManagerRepository
                .findFirstByOrderByAssignNumberDesc()
                .map(HubDeliveryManager::getAssignNumber)  // 가장 큰 assignNumber 조회
                .orElse(0);  // 값이 없으면 0

        // 새로운 assignNumber 설정
        int newAssignNumber = lastAssignNumber + 1;
        HubDeliveryManager hubDeliveryManager = createConsumerDeliveryManagerRequestDto.toEntity(
                createConsumerDeliveryManagerRequestDto,newAssignNumber);

        hubDeliveryManager.updateCreateData(passportDto.getUserId());

        hubDeliveryManagerRepository.save(hubDeliveryManager);

    }

    @Transactional
    public void update(
            UpdateHubDeliveryManagerRequestDto updateHubDeliveryManagerRequestDto,
            UUID hubDeliveryManagerId,
            String passport
    ) throws HubDuplicateAssignNumber {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        HubDeliveryManager hubDeliveryManager = getHubDeliveryManager(hubDeliveryManagerId);
        if(!hubDeliveryManagerRepository.isExistAssign_number(updateHubDeliveryManagerRequestDto.getAssign_number())){
            updateHubDeliveryManagerRequestDto.update(hubDeliveryManager);
        }else{
            throw new HubDuplicateAssignNumber(ExceptionMessage.DUPLICATE_ASSIGN_NUMBER);
        }
        hubDeliveryManager.updateModifiedData(passportDto.getUserId());
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
    public void delete(UUID consumerDeliveryManagerId,
                       String passport
    ) throws HubDeliveryManagerException.HubIsNotAvailableStatusException {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        HubDeliveryManager hubDeliveryManager = getHubDeliveryManager(consumerDeliveryManagerId);
        if (!hubDeliveryManager.getIsAvaliable()) {
            throw new HubDeliveryManagerException.HubIsNotAvailableStatusException(ExceptionMessage.IS_NOT_AVAILABLE);
        }
        hubDeliveryManager.softDeleteData(passportDto.getUserId());
    }

    private HubDeliveryManager getHubDeliveryManager(UUID hubDeliveryManagerId) {
        return hubDeliveryManagerRepository.findById(hubDeliveryManagerId)
                                           .orElseThrow(
                                                   () -> new HubDeliveryManagerException.HubDeliveryManagerNotFoundException(
                                                           Manager_NOT_FOUND_MESSAGE));
    }

    @Transactional
    public AlarmCreateResponseDto assign(DeliveryManagerRequestDto deliveryManagerRequestDto,
                                         String passport
    ) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        Optional<HubDeliveryManager> optionalManager = hubDeliveryManagerRepository.findFirstByOrderByAssignNumberDesc();

        // 담당자가 없는 경우 예외 처리 또는 기본 응답
        if (optionalManager.isEmpty()) {
            throw new HubDeliveryManagerException.ConsumerEmptyException(ExceptionMessage.HUB_NOT_ENOUGH);
        }
        HubDeliveryManager manager = optionalManager.get();
        manager.setIsAvaliable(false);
        manager.updateModifiedData(passportDto.getUserId());
        log.info("manager channelId = {} , number ={} " , manager.getChannelId(), manager.getUserPhoneNumber());

        AlarmCreateRequestDto alarmCreateRequestDto = AlarmCreateRequestDto.toDto(deliveryManagerRequestDto, manager);
        AlarmCreateResponseDto alarmCreateResponseDto = alarmClient.createAlarmForHubDeliver(alarmCreateRequestDto, passport)
                                                 .getData();

        log.info("alarmCreateResponseDto.getDeliverChannelId()={} alarmCreateResponseDto.getDeliverPhoneNumber={}",
                 alarmCreateResponseDto.getDeliverChannelId(), alarmCreateResponseDto.getDeliverPhoneNumber());

        return AlarmCreateResponseDto.from(manager,deliveryManagerRequestDto,alarmCreateResponseDto);
    }

    @Transactional
    public void returnHub(UUID deliveryManagerId) {
        HubDeliveryManager hubDeliveryManager = getHubDeliveryManager(deliveryManagerId);
        hubDeliveryManager.setIsAvaliable(true);

    }
}
