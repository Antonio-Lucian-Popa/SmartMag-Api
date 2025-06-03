package com.asusoftware.SmartMag_Api.notification.service;

import com.asusoftware.SmartMag_Api.exception.ResourceNotFoundException;
import com.asusoftware.SmartMag_Api.notification.model.Notification;
import com.asusoftware.SmartMag_Api.notification.model.dto.NotificationMessageDto;
import com.asusoftware.SmartMag_Api.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository repository;
    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(UUID userId, String message, String type) {
        Notification notification = repository.save(Notification.builder()
                .userId(userId)
                .message(message)
                .type(type)
                .build());

        NotificationMessageDto dto = new NotificationMessageDto(
                notification.getId(),
                notification.getMessage(),
                notification.getType(),
                notification.isRead(),
                notification.getCreatedAt()
        );

        messagingTemplate.convertAndSend("/topic/notifications/" + userId, dto);
    }

    public List<NotificationMessageDto> getAllForUser(UUID userId) {
        return repository.findAllByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(n -> new NotificationMessageDto(
                        n.getId(), n.getMessage(), n.getType(), n.isRead(), n.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public void markAsRead(UUID notificationId) {
        Notification notification = repository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        notification.setRead(true);
        repository.save(notification);
    }
}

