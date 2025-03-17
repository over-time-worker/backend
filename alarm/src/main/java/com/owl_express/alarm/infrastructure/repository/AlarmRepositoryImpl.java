package com.owl_express.alarm.infrastructure.repository;

import com.owl_express.alarm.domain.entity.Alarm;
import com.owl_express.alarm.domain.entity.Alarm;
import com.owl_express.alarm.domain.repository.AlarmRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AlarmRepositoryImpl implements AlarmRepository {
    private final AlarmJpaRepository alarmJpaRepository;
    private final AlarmQueryRepository alarmQueryRepository;

    public Alarm save(Alarm alarm) {
        return alarmJpaRepository.save(alarm);
    }

    public Optional<Alarm> findById(UUID alarmId) {
        return alarmJpaRepository.findById(alarmId);
    }

    public Optional<Alarm> findByMessageId(String messageId) {
        return alarmJpaRepository.findByMessageId(messageId);
    }
}
