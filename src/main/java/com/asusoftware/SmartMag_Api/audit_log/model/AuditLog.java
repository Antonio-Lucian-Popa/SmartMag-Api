package com.asusoftware.SmartMag_Api.audit_log.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entitate care reprezintă un log de audit pentru o acțiune efectuată în sistem.")
public class AuditLog {

    @Id
    @GeneratedValue
    @Schema(description = "ID-ul unic al log-ului", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private UUID id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "ID-ul utilizatorului care a efectuat acțiunea", example = "a60f32a1-3855-4bcf-a678-1d4f08472dc3")
    private UUID userId;

    @Column(nullable = false)
    @Schema(description = "Acțiunea efectuată (ex: CREATE_SHIFT_SWAP, DELETE_PRODUCT)", example = "UPDATE_TIME_OFF")
    private String action;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Detalii despre acțiunea efectuată", example = "Cererea de concediu a fost actualizată.")
    private String details;

    @Column(nullable = false)
    @Schema(description = "Data și ora la care s-a produs acțiunea", example = "2025-06-02T14:23:45")
    private LocalDateTime timestamp;
}

