package com.owlexpress.delivery.application.service;

import static com.owlexpress.delivery.common.exception.ExceptionMessage.DELIVERY_DELETE_FAIL_MESSAGE;
import static com.owlexpress.delivery.common.exception.ExceptionMessage.DELIVERY_HISTORY_NOT_FOUND_MESSAGE;
import static com.owlexpress.delivery.common.exception.ExceptionMessage.DELIVERY_NOT_FOUND_MESSAGE;

import com.owlexpress.delivery.application.dtos.request.DeliveryCompleteRequestDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryCreateRequestDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryCreateRequestDto.HubListDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryManagerRequestDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryUpdateRequestDto;
import com.owlexpress.delivery.application.dtos.response.DeliveryFindResponseDto;
import com.owlexpress.delivery.application.exceptions.DeliveryException.DeliveryDeleteFailException;
import com.owlexpress.delivery.application.exceptions.DeliveryException.DeliveryHistoryNotFoundException;
import com.owlexpress.delivery.application.exceptions.DeliveryException.DeliveryNotFoundException;
import com.owlexpress.delivery.common.helper.PassportHelper;
import com.owlexpress.delivery.common.util.PageUtil;
import com.owlexpress.delivery.domain.entity.Delivery;
import com.owlexpress.delivery.domain.entity.Delivery.DeliveryStatus;
import com.owlexpress.delivery.domain.entity.DeliveryHistory;
import com.owlexpress.delivery.domain.repository.DeliveryRepository;
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
    private final PassportHelper passportHelper;

    @Override
    @Transactional
    public void create(
            DeliveryCreateRequestDto deliveryCreateRequestDto,
            String passport
    ) {
        Long userId = passportHelper.getPassportDto(passport).getUserId();

        Delivery delivery = Delivery.create(deliveryCreateRequestDto, DeliveryStatus.PENDING_AT_HUB, userId);
        createDeliveryHistory(delivery, deliveryCreateRequestDto, userId);
    }

    @Override
    @Transactional
    public void update(
            UUID deliveryId,
            DeliveryUpdateRequestDto deliveryUpdateRequestDto,
            String passport
    ) {
        Long userId = passportHelper.getPassportDto(passport).getUserId();

        Delivery delivery = getDeliveryById(deliveryId);
        delivery.updateDeliveryStatus(DeliveryStatus.getStatus(deliveryUpdateRequestDto.getDeliveryStatus()), userId);
    }

    @Override
    @Transactional
    public void delete(
            UUID deliveryId,
            String passport
    ) {
        Long userId = passportHelper.getPassportDto(passport).getUserId();

        Delivery delivery = getDeliveryById(deliveryId);

        if(delivery.getDeliveryStatus().equals(DeliveryStatus.PENDING_AT_HUB)) {
            delivery.deleteDelivery(userId);
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
    public void startHubDelivery(
            UUID deliveryId,
            UUID deliveryHistoryId,
            String passport
    ) {
        Long userId = passportHelper.getPassportDto(passport).getUserId();

        Delivery delivery = getDeliveryByIdWithDeliveryHistories(deliveryId);
        delivery.updateDeliveryStatus(DeliveryStatus.SHIPPING_TO_HUB, userId);

        DeliveryHistory deliveryHistory = getDeliveryHistoryById(deliveryHistoryId, delivery.getDeliveryHistories());
        delivery.updateDeliveryHistoryStatus(deliveryHistory ,DeliveryStatus.SHIPPING_TO_HUB ,userId);
    }

    @Override
    @Transactional
    public void startCompanyDelivery(
            UUID deliveryId,
            UUID deliveryHistoryId,
            String passport
    ) {
        Long userId = passportHelper.getPassportDto(passport).getUserId();

        Delivery delivery = getDeliveryByIdWithDeliveryHistories(deliveryId);
        delivery.updateDeliveryStatus(DeliveryStatus.SHIPPING_TO_COMPANY, userId);

        DeliveryHistory deliveryHistory = getDeliveryHistoryById(deliveryHistoryId, delivery.getDeliveryHistories());
        delivery.updateDeliveryHistoryStatus(deliveryHistory ,DeliveryStatus.SHIPPING_TO_COMPANY ,userId);
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

        Delivery delivery = getDeliveryByIdWithDeliveryHistories(deliveryId);
        delivery.updateDeliveryStatus(DeliveryStatus.ARRIVED_AT_HUB, userId);

        List<DeliveryHistory> deliveryHistoryList = delivery.getDeliveryHistories();

        DeliveryHistory deliveryHistory = getDeliveryHistoryById(deliveryHistoryId, deliveryHistoryList);
        delivery.updateDeliveryHistoryActualInfo(deliveryHistory ,DeliveryStatus.COMPLETE, requestDto ,userId);

        // TODO : 배송 담당자 서비스에 FeignClient로 배송 담당자 반환 처리
        //반환 실패시 예외 처리

        // 꺼내온 허브가 마지막 순서라면 ?
        if(deliveryHistoryList.indexOf(deliveryHistory) == deliveryHistoryList.size() - 1) {

            DeliveryManagerRequestDto deliveryManagerRequestDto = DeliveryManagerRequestDto.toDeliveryManagerRequestDto(
                    delivery,
                    deliveryHistory,
                    delivery.getDeliveryHistories()
            );
            // TODO : 배송 담당자 서비스에 FeignClient로 업체 배송 담당자 요청
            // 응답 예외 처리

            delivery.updateCompanyDeliver(UUID.randomUUID(), userId);
        } else {
            // TODO : 배송 담당자 서비스에 FeignClient로 허브 배송 담당자 요청
            // 요청 실패시 예외 처리
        }
    }

    @Override
    @Transactional
    public void completeCompanyDelivery(
            UUID deliveryId,
            UUID deliveryHistoryId,
            DeliveryCompleteRequestDto requestDto,
            String passport
    ) {
        // TODO : 배송 담당자 서비스에 FeignClient로 배송 담당자 반환 처리
        //반환 실패시 예외 처리

        Long userId = passportHelper.getPassportDto(passport).getUserId();

        Delivery delivery = getDeliveryByIdWithDeliveryHistories(deliveryId);
        delivery.updateDeliveryStatus(DeliveryStatus.COMPLETE, userId);

        DeliveryHistory deliveryHistory = getDeliveryHistoryById(deliveryHistoryId, delivery.getDeliveryHistories());
        delivery.updateDeliveryHistoryActualInfo(deliveryHistory ,DeliveryStatus.COMPLETE , requestDto, userId);
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

        DeliveryManagerRequestDto deliveryManagerRequestDto = DeliveryManagerRequestDto.toDeliveryManagerRequestDto(
                delivery,
                deliveryHistoryList.get(0),
                delivery.getDeliveryHistories()
        );
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
