package com.asusoftware.SmartMag_Api.audit_log.service;

import com.asusoftware.SmartMag_Api.audit_log.model.AuditLog;
import com.asusoftware.SmartMag_Api.audit_log.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public void log(UUID userId, String action, String details) {
        AuditLog log = AuditLog.builder()
                .userId(userId)
                .action(action)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);
    }
}

