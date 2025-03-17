package com.owl_express.alarm.infrastructure.repository;

import com.owl_express.alarm.domain.entity.Notification;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmJpaRepository extends JpaRepository<Notification, UUID> {
    Optional<Notification> findByMessageId(String messageId);
}
