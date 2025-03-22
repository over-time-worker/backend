package com.owl_express.alarm.application.service;

import com.owl_express.alarm.application.dtos.request.AlarmCreateRequestDto;
import com.owl_express.alarm.application.dtos.request.HubDeliverFallbackMessageCreateRequestDto;
import com.owl_express.alarm.application.dtos.response.AlarmCreateResponseDto;
import com.owl_express.alarm.application.dtos.response.AlarmFindResponseDto;
import com.owl_express.alarm.application.dtos.response.AlarmSearchResponseDto;
import java.util.UUID;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

@Service
public interface AlarmService {
    void createAlarmForHubDeliver(
            AlarmCreateRequestDto alarmCreateRequestDto,
            String passport
            );

    AlarmCreateResponseDto createAlarmForCompanyDeliver(
            AlarmCreateRequestDto alarmCreateRequestDto,
            String passport
    );

    void delete(
            String channelId,
            String messageId,
            String passport
    );

    AlarmFindResponseDto find(UUID alarmId);

    PagedModel<AlarmSearchResponseDto> search(
            int page,
            int size,
            String sort,
            String orderBy,
            String startDate,
            String endDate,
            String deliveryUserId,
            String platformType
    );

    void hubFallback(
            HubDeliverFallbackMessageCreateRequestDto requestDto,
            String passport
    );
}
