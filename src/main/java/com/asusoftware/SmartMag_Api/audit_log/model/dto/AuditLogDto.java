package com.asusoftware.SmartMag_Api.audit_log.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pentru returnarea informațiilor despre log-urile de audit.
 */
@Data
@Builder
public class AuditLogDto {
    private UUID id;
    private UUID userId;
    private String action;
    private String details;
    private LocalDateTime createdAt;
}