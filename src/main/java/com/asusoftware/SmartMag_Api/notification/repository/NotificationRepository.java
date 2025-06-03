package com.asusoftware.SmartMag_Api.notification.repository;

import com.asusoftware.SmartMag_Api.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findAllByUserIdOrderByCreatedAtDesc(UUID userId);
}

