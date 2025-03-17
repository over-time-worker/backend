package com.owl_express.alarm.domain.repository;

import com.owl_express.alarm.domain.entity.Notification;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository {
    Notification save(Notification notification);
    Optional<Notification> findByMessageId(String messageId);
}
