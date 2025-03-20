package com.owlexpress.deliverymanager.presentation.dto.request;

import com.owlexpress.deliverymanager.domain.entity.HubDeliveryManager;
import lombok.Data;

@Data
//유저정보는 이벤트로인해 알아서 올테니 할당번호만 변경 가능
public class UpdateHubDeliveryManagerRequestDto {
    private Integer assign_number;

    public void update(HubDeliveryManager hubDeliveryManager) {
        hubDeliveryManager.setAssignNumber(assign_number);
    }
}
