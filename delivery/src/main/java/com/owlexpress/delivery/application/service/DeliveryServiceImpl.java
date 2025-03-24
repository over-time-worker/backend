package com.owlexpress.delivery.application.service;

import static com.owlexpress.delivery.common.exception.ExceptionMessage.DELIVERY_DELETE_FAIL_MESSAGE;
import static com.owlexpress.delivery.common.exception.ExceptionMessage.DELIVERY_HISTORY_NOT_FOUND_MESSAGE;
import static com.owlexpress.delivery.common.exception.ExceptionMessage.DELIVERY_MANAGER_RETURN_FAIL_MESSAGE;
import static com.owlexpress.delivery.common.exception.ExceptionMessage.DELIVERY_NOT_FOUND_MESSAGE;
import static com.owlexpress.delivery.infrastructure.config.RedissonConfig.DELIVERY_IDLE_HOURS;
import static com.owlexpress.delivery.infrastructure.config.RedissonConfig.DELIVERY_TTL_HOURS;

import com.owlexpress.delivery.application.dtos.DeliveryCacheDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryCompleteRequestDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryCreateRequestDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryCreateRequestDto.HubListDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryManagerRequestDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryUpdateRequestDto;
import com.owlexpress.delivery.application.dtos.request.HubDeliverFallbackMessageCreateRequestDto;
import com.owlexpress.delivery.application.dtos.response.AlarmCreateResponseDto;
import com.owlexpress.delivery.application.dtos.response.DeliveryCreateResponseDto;
import com.owlexpress.delivery.application.dtos.response.DeliveryFindResponseDto;
import com.owlexpress.delivery.application.exceptions.DeliveryException.DeliverReturnFailException;
import com.owlexpress.delivery.application.exceptions.DeliveryException.DeliveryDeleteFailException;
import com.owlexpress.delivery.application.exceptions.DeliveryException.DeliveryHistoryNotFoundException;
import com.owlexpress.delivery.application.exceptions.DeliveryException.DeliveryNotFoundException;
import com.owlexpress.delivery.common.helper.PassportHelper;
import com.owlexpress.delivery.common.util.PageUtil;
import com.owlexpress.delivery.domain.entity.Delivery;
import com.owlexpress.delivery.domain.entity.Delivery.DeliveryStatus;
import com.owlexpress.delivery.domain.entity.DeliveryHistory;
import com.owlexpress.delivery.domain.repository.DeliveryRepository;
import feign.FeignException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final PassportHelper passportHelper;
    private final DeliveryUsecase deliveryUsecase;
    private final RedissonClient redissonClient;

    @Override
    @Transactional
    public DeliveryCreateResponseDto create(
            DeliveryCreateRequestDto deliveryCreateRequestDto,
            String passport
    ) {
        Long userId = passportHelper.getPassportDto(passport).getUserId();

        Delivery delivery = Delivery.create(deliveryCreateRequestDto, DeliveryStatus.PENDING_AT_HUB, userId);
        createDeliveryHistory(delivery, deliveryCreateRequestDto, userId, passport);

        return DeliveryCreateResponseDto.toDto(delivery);
    }

    @Override
    @Transactional
    public void update(
            UUID deliveryId,
            DeliveryUpdateRequestDto deliveryUpdateRequestDto,
            String passport
    ) {
        Long userId = passportHelper.getPassportDto(passport).getUserId();

        Delivery delivery = getCachedDelivery(deliveryId);
        delivery.updateDeliveryStatus(DeliveryStatus.getStatus(deliveryUpdateRequestDto.getDeliveryStatus()), userId);
        removeCachedDelivery(deliveryId);
    }

    @Override
    @Transactional
    public void delete(
            UUID deliveryId,
            String passport
    ) {
        Long userId = passportHelper.getPassportDto(passport).getUserId();

        Delivery delivery = getCachedDelivery(deliveryId);

        if(!delivery.getDeliveryStatus().equals(DeliveryStatus.PENDING_AT_HUB)) {
            throw new DeliveryDeleteFailException(DELIVERY_DELETE_FAIL_MESSAGE);
        }

        delivery.deleteDelivery(userId);
        removeCachedDelivery(deliveryId);
    }

    @Override
    public DeliveryFindResponseDto find(UUID deliveryId) {

        Delivery delivery = getCachedDelivery(deliveryId);

        return DeliveryFindResponseDto.toDto(delivery);

    }

    @Override
    public PagedModel<DeliveryFindResponseDto> search(
            int page,
            int size,
            String sort,
            String orderBy,
            String startDate,
            String endDate,
            UUID deliveryId,
            String deliveryStatus
    ) {

        Pageable pageable = PageUtil.getPageable(page, size, sort, orderBy);
        Page<DeliveryFindResponseDto> paged = deliveryRepository.search(pageable, startDate, endDate, deliveryId, deliveryStatus);
        return new PagedModel<>(paged);
    }

    @Override
    @Transactional
    public void startHubDelivery(
            UUID deliveryId,
            UUID deliveryHistoryId,
            String passport
    ) {
        Long userId = passportHelper.getPassportDto(passport).getUserId();

        Delivery delivery = getCachedDeliveryWithDeliveryHistory(deliveryId);
        delivery.updateDeliveryStatus(DeliveryStatus.SHIPPING_TO_HUB, userId);

        DeliveryHistory deliveryHistory = getDeliveryHistoryById(deliveryHistoryId, delivery.getDeliveryHistories());
        delivery.updateDeliveryHistoryStatus(deliveryHistory ,DeliveryStatus.SHIPPING_TO_HUB ,userId);

        deliveryRepository.save(delivery);
        removeCachedDelivery(deliveryId);
    }

    @Override
    @Transactional
    public void startCompanyDelivery(
            UUID deliveryId,
            UUID deliveryHistoryId,
            String passport
    ) {
        Long userId = passportHelper.getPassportDto(passport).getUserId();

        Delivery delivery = getCachedDeliveryWithDeliveryHistory(deliveryId);
        delivery.updateDeliveryStatus(DeliveryStatus.SHIPPING_TO_COMPANY, userId);

        DeliveryHistory deliveryHistory = getDeliveryHistoryById(deliveryHistoryId, delivery.getDeliveryHistories());
        delivery.updateDeliveryHistoryStatus(deliveryHistory ,DeliveryStatus.SHIPPING_TO_COMPANY ,userId);

        deliveryRepository.save(delivery);
        removeCachedDelivery(deliveryId);
    }

    @Override
    @Transactional
    public void completeHubDelivery(
            UUID deliveryId,
            UUID deliveryHistoryId,
            DeliveryCompleteRequestDto requestDto,
            String passport
    ) {
        Long userId = passportHelper.getPassportDto(passport).getUserId();

        Delivery delivery = getCachedDeliveryWithDeliveryHistory(deliveryId);
        delivery.updateDeliveryStatus(DeliveryStatus.ARRIVED_AT_HUB, userId);

        List<DeliveryHistory> deliveryHistoryList = delivery.getDeliveryHistories();

        DeliveryHistory deliveryHistory = getDeliveryHistoryById(deliveryHistoryId, deliveryHistoryList);
        delivery.updateDeliveryHistoryActualInfo(deliveryHistory ,DeliveryStatus.COMPLETE, requestDto ,userId);

        AlarmCreateResponseDto alarmCreateResponseDto;
        DeliveryHistory nextHistory = deliveryHistoryList.get(deliveryHistoryList.indexOf(deliveryHistory) + 1);
        Boolean isHubDeliver = true;

        if(deliveryHistoryList.indexOf(deliveryHistory) == deliveryHistoryList.size() - 2) {
            isHubDeliver = false;

            DeliveryManagerRequestDto deliveryManagerRequestDto = DeliveryManagerRequestDto.toDeliveryManagerRequestDto(
                    delivery,
                    nextHistory,
                    delivery.getDeliveryHistories()
            );

            alarmCreateResponseDto = deliveryUsecase.assignCompanyDeliverFromDeliveryManager(deliveryManagerRequestDto, passport);
            delivery.updateCompanyDeliverInfo(nextHistory, alarmCreateResponseDto, userId);

            delivery.updateCompanyDeliver(alarmCreateResponseDto.getDeliverId(), userId);

        } else {
            DeliveryManagerRequestDto deliveryManagerRequestDto = DeliveryManagerRequestDto.toDeliveryManagerRequestDto(
                    delivery,
                    nextHistory,
                    delivery.getDeliveryHistories()
            );

            alarmCreateResponseDto = deliveryUsecase.assignHubDeliverFromDeliveryManager(deliveryManagerRequestDto, passport);
            delivery.updateHubDeliverInfo(nextHistory, alarmCreateResponseDto, userId);
        }

        try {
            deliveryUsecase.returnHubDeliverToDeliveryManager(deliveryHistory.getDeliverId());
        } catch(FeignException e) {

            if(isHubDeliver) {
                deliveryUsecase.returnHubDeliverToDeliveryManager(alarmCreateResponseDto.getDeliverId());

                deliveryUsecase.sendFallbackHubDeliverMessageToAlarm(
                        HubDeliverFallbackMessageCreateRequestDto.toDto(alarmCreateResponseDto),
                        passport
                );
            } else{
                deliveryUsecase.returnCompanyDeliverToDeliveryManager(alarmCreateResponseDto.getDeliverId(), passport);

                deliveryUsecase.DeleteCompanyDeliverMessageToAlarm(
                        alarmCreateResponseDto.getDeliverChannelId(),
                        alarmCreateResponseDto.getPlatformMessageId(),
                        passport
                );
            }

            throw new DeliverReturnFailException(DELIVERY_MANAGER_RETURN_FAIL_MESSAGE);

        }

        deliveryRepository.save(delivery);
        removeCachedDelivery(deliveryId);
    }

    @Override
    @Transactional
    public void completeCompanyDelivery(
            UUID deliveryId,
            UUID deliveryHistoryId,
            DeliveryCompleteRequestDto requestDto,
            String passport
    ) {
        Long userId = passportHelper.getPassportDto(passport).getUserId();

        Delivery delivery = getCachedDeliveryWithDeliveryHistory(deliveryId);
        delivery.updateDeliveryStatus(DeliveryStatus.COMPLETE, userId);

        DeliveryHistory deliveryHistory = getDeliveryHistoryById(deliveryHistoryId, delivery.getDeliveryHistories());
        delivery.updateDeliveryHistoryActualInfo(deliveryHistory ,DeliveryStatus.COMPLETE , requestDto, userId);

        deliveryUsecase.returnCompanyDeliverToDeliveryManager(deliveryHistory.getDeliverId(), passport);

        deliveryRepository.save(delivery);
        removeCachedDelivery(deliveryId);
    }

    @Override
    public Boolean findByConsumer(UUID consumerId) {

        return deliveryRepository.isExistByConsumerCompanyId(consumerId);
    }

    @Transactional
    public void createDeliveryHistory(
            Delivery delivery,
            DeliveryCreateRequestDto deliveryCreateRequestDto,
            Long userId,
            String passport
    ) {
         List<HubListDto> hubListDtos = getHubList(deliveryCreateRequestDto);

         List<DeliveryHistory> deliveryHistoryList = DeliveryHistory.createDeliveryHistoryList(delivery, hubListDtos, userId);
         delivery.updateDeliverHistoryList(deliveryHistoryList);

         DeliveryHistory firstDeliveryHistory = deliveryHistoryList.get(0);
         DeliveryManagerRequestDto deliveryManagerRequestDto = DeliveryManagerRequestDto.toDeliveryManagerRequestDto(
                 delivery,
                 firstDeliveryHistory,
                 delivery.getDeliveryHistories()
         );

        AlarmCreateResponseDto alarmCreateResponseDto = assignDelivery(deliveryCreateRequestDto, deliveryManagerRequestDto, passport);

         delivery.updateHubDeliverInfo(firstDeliveryHistory, alarmCreateResponseDto, userId);
         Delivery savedDelivery = deliveryRepository.save(delivery);

        RMapCache<UUID, DeliveryCacheDto> cache = redissonClient.getMapCache("deliveryCacheDto");
        DeliveryCacheDto deliveryCacheDto = DeliveryCacheDto.toCacheDto(savedDelivery);
        cache.put(savedDelivery.getId(), deliveryCacheDto, DELIVERY_TTL_HOURS, TimeUnit.HOURS, DELIVERY_IDLE_HOURS, TimeUnit.HOURS);
    }

    private List<HubListDto> getHubList(DeliveryCreateRequestDto dto) {
        return dto.getDestinationHubId() == null
                ? List.of(dto.getHubList().get(0))
                : dto.getHubList();
    }

    private AlarmCreateResponseDto assignDelivery(DeliveryCreateRequestDto dto, DeliveryManagerRequestDto requestDto, String passport) {
        return dto.getDestinationHubId() == null
                ? deliveryUsecase.assignCompanyDeliverFromDeliveryManager(requestDto, passport)
                : deliveryUsecase.assignHubDeliverFromDeliveryManager(requestDto, passport);
    }

    private Delivery getDeliveryById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new DeliveryNotFoundException(DELIVERY_NOT_FOUND_MESSAGE));
    }

    private Delivery getDeliveryByIdWithDeliveryHistories(UUID deliveryId) {
        return deliveryRepository.findDeliveryByIdWithDeliveryHistories(deliveryId).orElseThrow(
                () -> new DeliveryNotFoundException(DELIVERY_NOT_FOUND_MESSAGE));
    }

    private DeliveryHistory getDeliveryHistoryById(UUID deliveryHistoryId, List<DeliveryHistory> deliveryHistories) {
        return deliveryHistories.stream()
                .filter(history -> history.getId().equals(deliveryHistoryId))
                .findFirst()
                .orElseThrow(() -> new DeliveryHistoryNotFoundException(DELIVERY_HISTORY_NOT_FOUND_MESSAGE));
    }

    private Delivery getCachedDelivery(UUID deliveryId) {
        RMapCache<UUID, DeliveryCacheDto> cache = redissonClient.getMapCache("deliveryCacheDto");
        DeliveryCacheDto deliveryCacheDto = cache.get(deliveryId);
        Delivery delivery;

        if(deliveryCacheDto == null) {
            delivery = getDeliveryById(deliveryId);
            cache.put(deliveryId, DeliveryCacheDto.toCacheDto(delivery) , DELIVERY_TTL_HOURS, TimeUnit.HOURS, DELIVERY_IDLE_HOURS, TimeUnit.HOURS);

        } else {
            delivery = Delivery.toEntity(deliveryCacheDto);
        }
        return delivery;
    }

    private Delivery getCachedDeliveryWithDeliveryHistory(UUID deliveryId) {
        RMapCache<UUID, DeliveryCacheDto> cache = redissonClient.getMapCache("deliveryCacheDto");
        DeliveryCacheDto deliveryCacheDto = cache.get(deliveryId);
        Delivery delivery;

        if(deliveryCacheDto == null) {
            delivery = getDeliveryByIdWithDeliveryHistories(deliveryId);
            cache.put(deliveryId, DeliveryCacheDto.toCacheDto(delivery) , DELIVERY_TTL_HOURS, TimeUnit.HOURS, DELIVERY_IDLE_HOURS, TimeUnit.HOURS);

        } else {
            delivery = Delivery.toEntity(deliveryCacheDto);
            delivery.updateDeliveryToDeliveryHistory(delivery.getDeliveryHistories());
        }
        return delivery;
    }

    private void putDeliveryToCached(Delivery delivery) {
        RMapCache<UUID, DeliveryCacheDto> cache = redissonClient.getMapCache("deliveryCacheDto");
        cache.put(delivery.getId(), DeliveryCacheDto.toCacheDto(delivery) , DELIVERY_TTL_HOURS, TimeUnit.HOURS, DELIVERY_IDLE_HOURS, TimeUnit.HOURS);
    }

    private void removeCachedDelivery(UUID deliveryId) {
        RMapCache<UUID, DeliveryCacheDto> cache = redissonClient.getMapCache("deliveryCacheDto");
        cache.remove(deliveryId);
    }
}
