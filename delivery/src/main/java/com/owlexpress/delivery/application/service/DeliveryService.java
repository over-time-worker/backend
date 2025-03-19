package com.owlexpress.delivery.application.service;

import com.owlexpress.delivery.application.dtos.request.DeliveryCreateRequestDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryUpdateRequestDto;
import com.owlexpress.delivery.application.dtos.response.DeliveryFindResponseDto;
import java.util.UUID;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

@Service
public interface DeliveryService {

    void create(DeliveryCreateRequestDto deliveryCreateRequestDto);

    void update(UUID deliveryId, DeliveryUpdateRequestDto deliveryUpdateRequestDto);

    void delete(UUID deliveryId);

    DeliveryFindResponseDto find(UUID deliveryId);

    PagedModel<DeliveryFindResponseDto> search(int page, int size, String sort, String orderBy, String startDate, String endDate, UUID deliveryId, String deliveryStatus);

    void startHubDelivery(UUID deliveryId, UUID deliveryHistoryId);

    void startCompanyDelivery(UUID deliveryId, UUID deliveryHistoryId);

    void completeHubDelivery(UUID deliveryId, UUID deliveryHistoryId);

    void completeCompanyDelivery(UUID deliveryId, UUID deliveryHistoryId);

//    DeliveryHistoryCreateResponseDto createDeliveryHistory(Delivery delivery, DeliveryCreateRequestDto deliveryCreateRequestDto, Long userId);
}
