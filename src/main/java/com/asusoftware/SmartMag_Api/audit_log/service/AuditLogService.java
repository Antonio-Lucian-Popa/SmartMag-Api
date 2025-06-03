package com.asusoftware.SmartMag_Api.audit_log.service;

import com.asusoftware.SmartMag_Api.audit_log.model.AuditLog;
import com.asusoftware.SmartMag_Api.audit_log.model.dto.AuditLogDto;
import com.asusoftware.SmartMag_Api.audit_log.repository.AuditLogRepository;
import com.asusoftware.SmartMag_Api.exception.ResourceNotFoundException;
import com.asusoftware.SmartMag_Api.user.model.User;
import com.asusoftware.SmartMag_Api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    /**
     * Salvează un log în baza de date pentru o acțiune efectuată de un utilizator.
     */
    public void log(UUID userId, String action, String details) {
        AuditLog log = AuditLog.builder()
                .userId(userId)
                .action(action)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);
    }

    /**
     * Returnează toate log-urile pentru compania utilizatorului logat.
     * Folosește compania din contextul utilizatorului.
     */
    public List<AuditLogDto> getLogsForCompany(UUID keycloakId) {
        User requester = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UUID companyId = requester.getCompanyId();

        List<AuditLog> logs = auditLogRepository.findAllByCompanyId(companyId);

        return logs.stream()
                .map(log -> mapper.map(log, AuditLogDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Returnează log-urile doar pentru un anumit utilizator din compania curentă.
     * Se face și validare că utilizatorul aparține companiei.
     */
    public List<AuditLogDto> getLogsForUserInCompany(UUID userId, UUID keycloakId) {
        User requester = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        User target = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Target user not found"));

        if (!requester.getCompanyId().equals(target.getCompanyId())) {
            throw new AccessDeniedException("You do not have access to logs for this user.");
        }

        List<AuditLog> logs = auditLogRepository.findAllByUserId(userId);

        return logs.stream()
                .map(log -> mapper.map(log, AuditLogDto.class))
                .collect(Collectors.toList());
    }
}

