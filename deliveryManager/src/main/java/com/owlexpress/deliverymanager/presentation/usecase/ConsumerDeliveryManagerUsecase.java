package com.owlexpress.deliverymanager.presentation.usecase;

import com.owlexpress.deliverymanager.common.dto.request.CreateConsumerDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.common.dto.request.DeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.common.dto.request.UpdateConsumerDeliveryManagerRequestDto;
import com.owlexpress.deliverymanager.common.dto.response.AlarmCreateResponseDto;
import com.owlexpress.deliverymanager.common.dto.response.FindConsumerResponseDto;
import com.owlexpress.deliverymanager.common.exception.ConsumerDeliveryManagerException.ConsumerDeliveryManagerNotAvailableException;
import com.owlexpress.deliverymanager.common.exception.ConsumerDeliveryManagerException.ConsumerDuplicateAssignNumberException;
import com.owlexpress.deliverymanager.common.exception.ConsumerDeliveryManagerException.ConsumerEmptyException;
import com.owlexpress.deliverymanager.common.exception.ConsumerDeliveryManagerException.HubNotFoundException;
import com.owlexpress.deliverymanager.common.exception.ConsumerDeliveryManagerException.LockExistException;
import java.io.IOException;
import java.util.UUID;
import org.springframework.data.web.PagedModel;

public interface ConsumerDeliveryManagerUsecase {

    void create(CreateConsumerDeliveryManagerRequestDto consumerDeliveryManagerUsecase, String passport) throws HubNotFoundException;

    void update(UpdateConsumerDeliveryManagerRequestDto updateConsumerDeliveryManagerRequestDto, UUID consumerDeliveryManagerId, String passport) throws ConsumerDuplicateAssignNumberException;

    FindConsumerResponseDto find(UUID consumerDeliveryManagerId);

    PagedModel<FindConsumerResponseDto> search(Integer page, Integer size, String sort, String q, String orderBy);

    void delete(UUID consumerDeliveryManagerId, String passport) throws ConsumerDeliveryManagerNotAvailableException;

    AlarmCreateResponseDto assign(DeliveryManagerRequestDto deliveryManagerRequestDto, String passport) throws HubNotFoundException, ConsumerEmptyException, InterruptedException, LockExistException, IOException;

    void returnHub(UUID deliveryManagerId);
}
