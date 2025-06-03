package com.asusoftware.SmartMag_Api.audit_log.controller;

import com.asusoftware.SmartMag_Api.audit_log.model.dto.AuditLogDto;
import com.asusoftware.SmartMag_Api.audit_log.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Audit Log", description = "Operațiuni pentru log-uri de audit")
@RestController
@RequestMapping("/api/v1/audit-log")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @Operation(
            summary = "Listare log-uri companie",
            description = "Obține toate log-urile de audit pentru compania din care face parte utilizatorul curent. Doar OWNER sau MANAGER pot accesa.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Log-urile au fost returnate cu succes"),
                    @ApiResponse(responseCode = "403", description = "Acces interzis - nu ai rolul necesar")
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<List<AuditLogDto>> getCompanyAuditLogs(@AuthenticationPrincipal Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(auditLogService.getLogsForCompany(keycloakId));
    }

    @Operation(
            summary = "Listare log-uri pentru un utilizator",
            description = "Obține toate log-urile asociate unui utilizator din companie. Accesibil doar pentru OWNER sau MANAGER.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Log-urile utilizatorului au fost returnate cu succes"),
                    @ApiResponse(responseCode = "403", description = "Acces interzis - nu ai permisiunea necesară"),
                    @ApiResponse(responseCode = "404", description = "Utilizatorul nu a fost găsit")
            }
    )
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
