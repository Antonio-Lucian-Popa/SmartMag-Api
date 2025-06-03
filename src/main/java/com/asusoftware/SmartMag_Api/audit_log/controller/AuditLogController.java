package com.asusoftware.SmartMag_Api.audit_log.controller;

import com.asusoftware.SmartMag_Api.audit_log.model.dto.AuditLogDto;
import com.asusoftware.SmartMag_Api.audit_log.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller pentru gestionarea audit log-urilor.
 * Permite vizualizarea acțiunilor efectuate de utilizatori dintr-o companie.
 * Accesul este permis doar utilizatorilor cu rolul OWNER sau MANAGER.
 */
@RestController
@RequestMapping("/api/v1/audit-log")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    /**
     * Obține toate log-urile din sistem pentru compania utilizatorului logat.
     * Doar OWNER sau MANAGER pot accesa această rută.
     *
     * @param principal Token JWT al utilizatorului autentificat.
     * @return Lista de log-uri pentru compania din care face parte utilizatorul.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<List<AuditLogDto>> getCompanyAuditLogs(@AuthenticationPrincipal Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(auditLogService.getLogsForCompany(keycloakId));
    }

    /**
     * Obține toate log-urile asociate unui anumit utilizator din companie.
     * Poate fi folosit pentru tracking de acțiuni specifice ale unui angajat.
     * Accesul este permis doar OWNER și MANAGER din compania respectivă.
     *
     * @param userId     ID-ul utilizatorului pentru care se cer log-urile.
     * @param principal  Token JWT al utilizatorului logat.
     * @return Lista de log-uri asociate utilizatorului dat.
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<List<AuditLogDto>> getUserLogs(
            @PathVariable UUID userId,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(auditLogService.getLogsForUserInCompany(userId, keycloakId));
    }
}
