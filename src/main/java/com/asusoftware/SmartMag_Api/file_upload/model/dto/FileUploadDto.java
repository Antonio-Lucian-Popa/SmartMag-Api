package com.asusoftware.SmartMag_Api.file_upload.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pentru răspunsul trimis către client după uploadul unui fișier.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Informații despre un fișier încărcat")
public class FileUploadDto {

    @Schema(description = "ID-ul unic al fișierului", example = "b4ad1f48-22ce-4fd7-8a56-c53d4e1f6c3c")
    private UUID id;

    @Schema(description = "ID-ul utilizatorului care a încărcat fișierul", example = "aa13e8f9-6f91-4a9b-8880-775bfc1d02c6")
    private UUID userId;

    @Schema(description = "Numele original al fișierului", example = "contract.pdf")
    private String fileName;

    @Schema(description = "URL-ul public al fișierului", example = "/uploads/images/time_off_request/123e/file_contract.pdf")
    private String fileUrl;

    @Schema(description = "Tipul entității asociate", example = "time_off_request")
    private String entityType;

    @Schema(description = "UUID-ul entității asociate", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID entityId;

    @Schema(description = "Data și ora încărcării fișierului", example = "2025-06-02T13:45:00")
    private LocalDateTime uploadedAt;
}
