package com.owl_express.alarm.presentation;

import com.owl_express.alarm.common.dto.request.AlarmCreateRequestDto;
import com.owl_express.alarm.common.dto.request.HubDeliverFallbackMessageCreateRequestDto;
import com.owl_express.alarm.common.dto.response.AlarmCreateResponseDto;
import com.owl_express.alarm.common.dto.response.AlarmFindResponseDto;
import com.owl_express.alarm.common.dto.response.AlarmSearchResponseDto;
import java.util.UUID;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

@Service
public interface AlarmService {
    AlarmCreateResponseDto createAlarmForHubDeliver(
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
