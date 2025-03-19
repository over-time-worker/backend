package com.owlexpress.delivery.application.service;

import static com.owlexpress.delivery.common.exception.ExceptionMessage.DELIVERY_DELETE_FAIL_MESSAGE;
import static com.owlexpress.delivery.common.exception.ExceptionMessage.DELIVERY_HISTORY_NOT_FOUND_MESSAGE;
import static com.owlexpress.delivery.common.exception.ExceptionMessage.DELIVERY_NOT_FOUND_MESSAGE;

import com.owlexpress.delivery.application.dtos.request.DeliveryCreateRequestDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryCreateRequestDto.HubListDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryUpdateRequestDto;
import com.owlexpress.delivery.application.dtos.response.DeliveryFindResponseDto;
import com.owlexpress.delivery.application.exceptions.DeliveryException.DeliveryDeleteFailException;
import com.owlexpress.delivery.application.exceptions.DeliveryException.DeliveryHistoryNotFoundException;
import com.owlexpress.delivery.application.exceptions.DeliveryException.DeliveryNotFoundException;
import com.owlexpress.delivery.common.util.PageUtil;
import com.owlexpress.delivery.domain.entity.Delivery;
import com.owlexpress.delivery.domain.entity.Delivery.DeliveryStatus;
import com.owlexpress.delivery.domain.entity.DeliveryHistory;
import com.owlexpress.delivery.domain.repository.DeliveryRepository;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    @Transactional
    public void create(DeliveryCreateRequestDto deliveryCreateRequestDto) {
        // TODO : create By 적용
        Delivery delivery = Delivery.create(deliveryCreateRequestDto, DeliveryStatus.PENDING_AT_HUB, 1L);
        createDeliveryHistory(delivery, deliveryCreateRequestDto, 1L);
    }

    @Override
    @Transactional
    public void update(UUID deliveryId, DeliveryUpdateRequestDto deliveryUpdateRequestDto) {
        Delivery delivery = getDeliveryById(deliveryId);
        // TODO : update By 적용
        delivery.updateDeliveryStatus(DeliveryStatus.getStatus(deliveryUpdateRequestDto.getDeliveryStatus()), 1L);
    }

    @Override
    @Transactional
    public void delete(UUID deliveryId) {

        Delivery delivery = getDeliveryById(deliveryId);

        if(delivery.getDeliveryStatus().equals(DeliveryStatus.PENDING_AT_HUB)) {
            delivery.deleteDelivery(1L);
            // TODO : 주문 서비스에 주문 삭제 요청
            //요청 실패시 예외처리

        } else {
            throw new DeliveryDeleteFailException(DELIVERY_DELETE_FAIL_MESSAGE);
        }
    }

    @Override
    public DeliveryFindResponseDto find(UUID deliveryId) {

        return DeliveryFindResponseDto.toDto(getDeliveryById(deliveryId));
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
    public void startHubDelivery(UUID deliveryId, UUID deliveryHistoryId) {

        Delivery delivery = getDeliveryByIdWithDeliveryHistories(deliveryId);
        // TODO : update By 적용
        delivery.updateDeliveryStatus(DeliveryStatus.SHIPPING_TO_HUB, 1L);

        DeliveryHistory deliveryHistory = getDeliveryHistoryById(deliveryHistoryId, delivery.getDeliveryHistories());
        delivery.updateDeliveryHistoryStatus(deliveryHistory ,DeliveryStatus.SHIPPING_TO_HUB ,1L);
    }

    @Override
    @Transactional
    public void startCompanyDelivery(UUID deliveryId, UUID deliveryHistoryId) {

        Delivery delivery = getDeliveryByIdWithDeliveryHistories(deliveryId);
        // TODO : update By 적용
        delivery.updateDeliveryStatus(DeliveryStatus.SHIPPING_TO_COMPANY, 1L);

        DeliveryHistory deliveryHistory = getDeliveryHistoryById(deliveryHistoryId, delivery.getDeliveryHistories());
        // TODO : update By 적용
        delivery.updateDeliveryHistoryStatus(deliveryHistory ,DeliveryStatus.SHIPPING_TO_COMPANY ,1L);
    }

    @Override
    @Transactional
    public void completeHubDelivery(UUID deliveryId, UUID deliveryHistoryId) {

        Delivery delivery = getDeliveryByIdWithDeliveryHistories(deliveryId);
        // TODO : update By 적용
        delivery.updateDeliveryStatus(DeliveryStatus.ARRIVED_AT_HUB, 1L);

        //TODO : 실제 이동 거리,시간 받아서 update
        List<DeliveryHistory> deliveryHistoryList = delivery.getDeliveryHistories();
        DeliveryHistory deliveryHistory = getDeliveryHistoryById(deliveryHistoryId, deliveryHistoryList);
        // TODO : update By 적용
        delivery.updateDeliveryHistoryStatus(deliveryHistory ,DeliveryStatus.COMPLETE ,1L);

        // TODO : 배송 담당자 서비스에 FeignClient로 배송 담당자 반환 처리
        //반환 실패시 예외 처리

        // TODO : 모든 허브 list 가져 온 후 (sequnce asc) : 배송 담당자 요청시 현재 허브정도, 다음 허브 정보 가공해서 넘겨야함 (요청사항 및 상품 정보 등등)
        // 꺼내온 허브가 마지막 순서라면 ?
        if(deliveryHistoryList.indexOf(deliveryHistory) == deliveryHistoryList.size() - 1) {

            // TODO : 배송 담당자 서비스에 FeignClient로 업체 배송 담당자 요청
            // 응답 예외 처리

            // TODO : update By 적용 / 받아온 companyDeliverId 적용
            delivery.updateCompanyDeliver(UUID.randomUUID(), 1L);
        } else {
            // TODO : 배송 담당자 서비스에 FeignClient로 허브 배송 담당자 요청
            // 요청 실패시 예외 처리
        }
    }

    @Override
    @Transactional
    public void completeCompanyDelivery(UUID deliveryId, UUID deliveryHistoryId) {
        // TODO : 배송 담당자 서비스에 FeignClient로 배송 담당자 반환 처리
        //반환 실패시 예외 처리

        //TODO : 실제 이동 거리,시간 받아서 update
        Delivery delivery = getDeliveryByIdWithDeliveryHistories(deliveryId);
        // TODO : update By 적용
        delivery.updateDeliveryStatus(DeliveryStatus.COMPLETE, 1L);

        DeliveryHistory deliveryHistory = getDeliveryHistoryById(deliveryHistoryId, delivery.getDeliveryHistories());
        // TODO : update By 적용
        delivery.updateDeliveryHistoryStatus(deliveryHistory ,DeliveryStatus.COMPLETE ,1L);
    }

    @Transactional
    public void createDeliveryHistory(
            Delivery delivery,
            DeliveryCreateRequestDto deliveryCreateRequestDto,
            Long userId
    ) {
         List<HubListDto> hubListDtos = deliveryCreateRequestDto.getHubList();
         List<DeliveryHistory> deliveryHistoryList = DeliveryHistory.createDeliveryHistoryList(delivery, hubListDtos, userId);

         delivery.updateDeliverHistoryList(deliveryHistoryList);
         deliveryRepository.save(delivery);

        // TODO : 배송 담당자 service로 첫 배송 담당자 배정 요청
        // 예외 응답 처리
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
}
