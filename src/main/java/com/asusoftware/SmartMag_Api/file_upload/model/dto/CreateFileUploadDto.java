package com.asusoftware.SmartMag_Api.file_upload.model.dto;

import lombok.Data;

import java.util.UUID;

/**
 * DTO pentru cererea de creare a unui fișier (doar input).
 */
@Data
public class CreateFileUploadDto {
    private String fileName;
    private String fileUrl;
    private String entityType;
    private UUID entityId;
}