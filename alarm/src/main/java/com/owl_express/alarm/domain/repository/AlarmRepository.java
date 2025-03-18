package com.owl_express.alarm.domain.repository;

import com.owl_express.alarm.application.dtos.response.AlarmSearchResponseDto;
import com.owl_express.alarm.domain.entity.Alarm;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository {
    Alarm save(Alarm alarm);

    Optional<Alarm> findById(UUID alarmId);

    Optional<Alarm> findByMessageId(String messageId);

    Page<AlarmSearchResponseDto> search(Pageable pageable, String startDate, String endDate,
            String deliveryUserId, String platformType);
}
