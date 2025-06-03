package com.asusoftware.SmartMag_Api.file_upload.model.dto;

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
public class FileUploadDto {
    private UUID id;
    private UUID userId;
    private String fileName;
    private String fileUrl;
    private String entityType;
    private UUID entityId;
    private LocalDateTime uploadedAt;
}
