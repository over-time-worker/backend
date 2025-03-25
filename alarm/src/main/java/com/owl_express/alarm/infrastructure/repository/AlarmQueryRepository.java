package com.owl_express.alarm.infrastructure.repository;

import com.owl_express.alarm.common.dto.response.AlarmSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmQueryRepository {

    Page<AlarmSearchResponseDto> search(Pageable pageable, String startDate, String endDate, String deliveryUserId, String platformType);
}
