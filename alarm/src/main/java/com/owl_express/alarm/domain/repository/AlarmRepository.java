package com.owl_express.alarm.domain.repository;

import com.owl_express.alarm.domain.entity.Alarm;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository {
    Alarm save(Alarm alarm);
    Optional<Alarm> findByMessageId(String messageId);
}
