package com.asusoftware.SmartMag_Api.file_upload.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

/**
 * DTO pentru cererea de creare a unui fișier (doar input).
 */
@Data
@Schema(description = "DTO pentru crearea manuală a unui fișier (doar input)")
public class CreateFileUploadDto {
    @Schema(description = "Numele original al fișierului", example = "factura.png")
    private String fileName;

    @Schema(description = "URL-ul public al fișierului", example = "/uploads/images/product/123/file_factura.png")
    private String fileUrl;

    @Schema(description = "Tipul entității asociate", example = "product")
    private String entityType;

    @Schema(description = "UUID-ul entității asociate", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID entityId;
}