package com.asusoftware.SmartMag_Api.file_upload.controller;

import com.asusoftware.SmartMag_Api.file_upload.model.dto.FileUploadDto;
import com.asusoftware.SmartMag_Api.file_upload.service.FileStorageService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileStorageService fileStorageService;

    /**
     * Upload un fișier și îl asociază unei entități.
     *
     * @param file        fișierul de uploadat
     * @param entityType  tipul entității (ex: "time_off_request", "product")
     * @param entityId    UUID-ul entității
     * @param principal   utilizatorul autentificat
     * @return detalii despre fișierul salvat
     */
    @PostMapping("/upload")
    public ResponseEntity<FileUploadDto> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("entityType") @NotBlank String entityType,
            @RequestParam("entityId") UUID entityId,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID userId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(fileStorageService.saveFile(file, userId, entityType, entityId));
    }

    /**
     * Obține toate fișierele asociate unei entități.
     *
     * @param entityType tipul entității
     * @param entityId   UUID-ul entității
     * @return lista de fișiere
     */
    @GetMapping
    public ResponseEntity<List<FileUploadDto>> getFilesForEntity(
            @RequestParam String entityType,
            @RequestParam UUID entityId
    ) {
        return ResponseEntity.ok(fileStorageService.getFilesForEntity(entityType, entityId));
    }

    /**
     * Șterge toate fișierele asociate unei entități.
     *
     * @param entityType tipul entității
     * @param entityId   UUID-ul entității
     * @return status 204 No Content
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteFilesForEntity(
            @RequestParam String entityType,
            @RequestParam UUID entityId
    ) {
        fileStorageService.deleteFilesForEntity(entityType, entityId);
        return ResponseEntity.noContent().build();
    }
}

