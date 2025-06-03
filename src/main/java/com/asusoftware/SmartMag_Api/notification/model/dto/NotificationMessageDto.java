package com.asusoftware.SmartMag_Api.notification.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessageDto {
    private UUID id;
    private String message;
    private String type;
    private boolean isRead;
    private LocalDateTime createdAt;
}

