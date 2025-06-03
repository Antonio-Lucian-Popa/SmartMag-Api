package com.asusoftware.SmartMag_Api.audit_log.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pentru returnarea informațiilor despre log-urile de audit.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Reprezintă un log de audit pentru o acțiune efectuată de un utilizator.")
public class AuditLogDto {

    @Schema(description = "ID-ul log-ului", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private UUID id;

    @Schema(description = "ID-ul utilizatorului care a efectuat acțiunea", example = "a60f32a1-3855-4bcf-a678-1d4f08472dc3")
    private UUID userId;

    @Schema(description = "Acțiunea efectuată (ex: CREATE_SHIFT_SWAP)", example = "DELETE_PRODUCT")
    private String action;

    @Schema(description = "Detalii suplimentare despre acțiune", example = "Produsul 'Apă minerală' a fost șters.")
    private String details;

    @Schema(description = "Data și ora la care acțiunea a fost înregistrată", example = "2025-06-02T14:23:45")
    private LocalDateTime createdAt;
}