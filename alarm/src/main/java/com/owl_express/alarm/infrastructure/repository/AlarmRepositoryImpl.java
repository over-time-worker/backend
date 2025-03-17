package com.owl_express.alarm.infrastructure.repository;

import com.owl_express.alarm.domain.entity.Notification;
import com.owl_express.alarm.domain.repository.AlarmRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AlarmRepositoryImpl implements AlarmRepository {
    private final AlarmJpaRepository alarmJpaRepository;
    private final AlarmQueryRepository alarmQueryRepository;

    public Notification save(Notification notification) {
        return alarmJpaRepository.save(notification);
    }

    public Optional<Notification> findByMessageId(String messageId) {
        return alarmJpaRepository.findByMessageId(messageId);
    }
}
