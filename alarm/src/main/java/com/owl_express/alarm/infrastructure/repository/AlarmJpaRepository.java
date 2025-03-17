package com.owl_express.alarm.infrastructure.repository;

import com.owl_express.alarm.domain.entity.Alarm;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmJpaRepository extends JpaRepository<Alarm, UUID> {
    Optional<Alarm> findByMessageId(String messageId);
}
