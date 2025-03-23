package com.owlexpress.deliverymanager.application.usecase;

import com.owlexpress.deliverymanager.application.dto.response.AlarmCreateResponseDto;
import com.owlexpress.deliverymanager.common.dto.request.AlarmCreateRequestDto;
import com.owlexpress.deliverymanager.common.dto.response.PassportDto;
import com.owlexpress.deliverymanager.common.exception.ConsumerDeliveryManagerException;
import com.owlexpress.deliverymanager.common.exception.ExceptionMessage;
import com.owlexpress.deliverymanager.common.helper.PassportHelper;
import com.owlexpress.deliverymanager.domain.dto.response.HubFindResponseDto;
import com.owlexpress.deliverymanager.domain.entity.ConsumerDeliveryManager;
import com.owlexpress.deliverymanager.domain.repository.ConsumerDeliveryManagerRepository;
import com.owlexpress.deliverymanager.infrastructure.CommonDto;
import com.owlexpress.deliverymanager.infrastructure.config.DeliveryManagerSearchConfig;
import com.owlexpress.deliverymanager.infrastructure.feignClient.AlarmClient;
import com.owlexpress.deliverymanager.infrastructure.feignClient.HubClient;
import com.owlexpress.deliverymanager.presentation.dto.request.CreateConsumerDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.request.DeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.request.UpdateConsumerDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.presentation.dto.response.FindConsumerResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.owlexpress.deliverymanager.common.exception.ConsumerDeliveryManagerException.*;
import static com.owlexpress.deliverymanager.common.exception.ExceptionMessage.Manager_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumerDeliveryManagerUsecase {
    private final ConsumerDeliveryManagerRepository consumerDeliveryManagerRepository;
    private final DeliveryManagerSearchConfig deliveryManagerSearchConfig;
    private final HubClient hubClient;
    private final PassportHelper passportHelper;
    private final AlarmClient alarmClient;
    private final RedissonClient redissonClient;

    @Transactional
    @CacheEvict(value = {"unassignedDeliveryManagers", "searchConsumerDeliveryManagers"}, allEntries = true)
    public void create(
            CreateConsumerDeliveryManagerRequestDto consumerDeliveryManagerUsecase,
            String passport
    ) throws HubNotFoundException {
        PassportDto passportDto = passportHelper.getPassportDto(passport);

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

        consumerDeliveryManager.updateCreateData(passportDto.getUserId());

        consumerDeliveryManagerRepository.save(consumerDeliveryManager);

    }


    @Transactional
    @CacheEvict(value = {"unassignedDeliveryManagers", "searchConsumerDeliveryManagers"}, allEntries = true)
    public void update(
            UpdateConsumerDeliveryManagerRequestDto updateConsumerDeliveryManagerRequestDto,
            UUID consumerDeliveryManagerId,
            String passport
    ) throws ConsumerDuplicateAssignNumberException {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        // 기존 배송 담당자 조회
        ConsumerDeliveryManager consumerDeliveryManager = getConsumerDeliveryManager(consumerDeliveryManagerId);

        // 할당 번호가 변경될 경우 중복 검사
        Integer newAssignNumber = updateConsumerDeliveryManagerRequestDto.getAssignNumber();
        if (!Objects.equals(consumerDeliveryManager.getAssignNumber(), newAssignNumber)) {
            if (validateAssignNumber(updateConsumerDeliveryManagerRequestDto)) {
                throw new ConsumerDuplicateAssignNumberException(ExceptionMessage.DUPLICATE_ASSIGN_NUMBER);
            }
            // 할당 번호 업데이트
            consumerDeliveryManager.setAssignNumber(newAssignNumber);
        }

        // 허브 존재 여부 확인 (존재하지 않으면 예외 발생)
        UUID newHubId = updateConsumerDeliveryManagerRequestDto.getHubId();
        if (newHubId != null) {
            boolean hubExists = Optional.ofNullable(hubClient.find(newHubId))
                                        .isPresent();
            if (!hubExists) {
                throw new IllegalArgumentException("존재하지 않는 허브 ID입니다: " + newHubId);
            }
            consumerDeliveryManager.setHubId(newHubId);
        }
        consumerDeliveryManager.updateModifiedData(passportDto.getUserId());
    }

    @Cacheable(value = "unassignedDeliveryManagers", key = "#consumerDeliveryManagerId")
    @Transactional(readOnly = true)
    public FindConsumerResponseDto find(UUID consumerDeliveryManagerId) {
        ConsumerDeliveryManager consumerDeliveryManager = getConsumerDeliveryManager(consumerDeliveryManagerId);
        return FindConsumerResponseDto.fromEntity(consumerDeliveryManager);
    }

    @Cacheable(value = "searchConsumerDeliveryManagers", key = "'page:' + #page + ':size:' + #size + ':sort:' + #sort + ':q:' + #q + ':orderBy:' + #orderBy")
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
    @CacheEvict(value = {"unassignedDeliveryManagers", "searchConsumerDeliveryManagers"}, allEntries = true)
    public void delete(
            UUID consumerDeliveryManagerId,
            String passport
    ) throws ConsumerDeliveryManagerNotAvailableException {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        ConsumerDeliveryManager consumerDeliveryManager = getConsumerDeliveryManager(consumerDeliveryManagerId);

        if (!consumerDeliveryManager.getIsAvaliable()) {
            throw new ConsumerDeliveryManagerNotAvailableException(ExceptionMessage.IS_NOT_AVAILABLE);
        }
        consumerDeliveryManager.softDeleteData(passportDto.getUserId());
    }

//    @Transactional
@CacheEvict(value = {"unassignedDeliveryManagers", "searchConsumerDeliveryManagers"}, allEntries = true)
    public AlarmCreateResponseDto assign(
            DeliveryManagerRequestDto deliveryManagerRequestDto,
            String passport
    ) throws HubNotFoundException, ConsumerEmptyException, InterruptedException, LockExistException {
        //허브별 단위 격리하기
        RLock lock = redissonClient.getLock("assignDeliveryManagerLock:" + deliveryManagerRequestDto.getCurrentHubId());
        boolean isLocked = false;
        int retryCount = 3;
        int attempts = 0;

        while (attempts < retryCount) {//3번정도 시도
            isLocked = lock.tryLock(3, 2, TimeUnit.SECONDS);
            if (isLocked) break;
            attempts++;
            Thread.sleep(500);  // optional backoff
        }

        if (!isLocked) {
            log.warn("배송 담당자 락 획득 실패 - HubId: {}, 시도 횟수: {}", deliveryManagerRequestDto.getCurrentHubId(), attempts);
            throw new LockExistException(ExceptionMessage.LOCK_IS_EXIST);
        }

        PassportDto passportDto = passportHelper.getPassportDto(passport);
        CommonDto<HubFindResponseDto> hubFindResponseDtoCommonDto = hubClient.find(
                deliveryManagerRequestDto.getCurrentHubId());

        // 허브가 존재하는지 확인
        if (hubFindResponseDtoCommonDto == null || hubFindResponseDtoCommonDto.getData() == null) {
            throw new HubNotFoundException(ExceptionMessage.HUB_NOT_FOUND);
        }

        // 가장 낮은 assignNumber를 가진 isAvailable = true인 담당자 조회
        UUID hubId = hubFindResponseDtoCommonDto.getData()
                                                .getHubId();
        Optional<ConsumerDeliveryManager> optionalManager = consumerDeliveryManagerRepository.findFirstByHubIdAndIsAvaliableTrueOrderByAssignNumberAsc(
                hubId);

        // 담당자가 없는 경우 예외 처리 또는 기본 응답
        if (optionalManager.isEmpty()) {
            throw new ConsumerDeliveryManagerException.ConsumerEmptyException(
                    ExceptionMessage.CONSUMER_NOT_ENOUGH + hubId);
        }
        ConsumerDeliveryManager manager = optionalManager.get();

        AlarmCreateRequestDto alarmCreateRequestDto = AlarmCreateRequestDto.toDto(deliveryManagerRequestDto, manager);
        AlarmCreateResponseDto alarmCreateResponseDto = alarmClient.createAlarmForCompanyDeliver(
                                                                       alarmCreateRequestDto, passport)
                                                               .getData();

        manager.setIsAvaliable(true);
        manager.updateModifiedData(passportDto.getUserId());

        // DTO 변환 및 반환
        return AlarmCreateResponseDto.from(manager, deliveryManagerRequestDto, alarmCreateResponseDto);
    }

    private boolean validateAssignNumber(UpdateConsumerDeliveryManagerRequestDto updateConsumerDeliveryManagerRequestDto) {
        return !consumerDeliveryManagerRepository.existByAssignNumber(
                updateConsumerDeliveryManagerRequestDto.getAssignNumber());
    }

    private ConsumerDeliveryManager getConsumerDeliveryManager(UUID consumerDeliveryManagerId) {
        return consumerDeliveryManagerRepository.findById(consumerDeliveryManagerId)
                                                .orElseThrow(() -> new ConsumerDeliveryManagerNotFoundException(
                                                        Manager_NOT_FOUND_MESSAGE));
    }

    @Transactional
    @CacheEvict(value = {"unassignedDeliveryManagers", "searchConsumerDeliveryManagers"}, allEntries = true)
    public void returnHub(UUID deliveryManagerId) {
        getConsumerDeliveryManager(deliveryManagerId).setIsAvaliable(true);
    }
}
