package com.owl_express.alarm.domain.repository;

import com.owl_express.alarm.domain.entity.Notification;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository {
    Notification save(Notification notification);
}
