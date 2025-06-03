package com.asusoftware.SmartMag_Api.file_upload.controller;

import com.asusoftware.SmartMag_Api.file_upload.model.dto.FileUploadDto;
import com.asusoftware.SmartMag_Api.file_upload.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Upload fișier",
            description = "Permite uploadul unui fișier asociat unei entități (ex: time_off_request, product).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fișier încărcat cu succes",
                    content = @Content(schema = @Schema(implementation = FileUploadDto.class))),
            @ApiResponse(responseCode = "400", description = "Date invalide sau fișier gol"),
            @ApiResponse(responseCode = "500", description = "Eroare la stocarea fișierului")
    })
    @PostMapping("/upload")
    public ResponseEntity<FileUploadDto> uploadFile(
            @Parameter(description = "Fișierul de încărcat", required = true)
            @RequestParam("file") MultipartFile file,

            @Parameter(description = "Tipul entității asociate (ex: time_off_request, product)", required = true)
            @RequestParam("entityType") @NotBlank String entityType,

            @Parameter(description = "UUID-ul entității", required = true)
            @RequestParam("entityId") UUID entityId,

            @Parameter(hidden = true)
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
    @Operation(summary = "Obține fișierele pentru o entitate",
            description = "Returnează lista de fișiere asociate unei entități specifice.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista fișierelor returnată cu succes",
                    content = @Content(schema = @Schema(implementation = FileUploadDto.class)))
    })
    @GetMapping
    public ResponseEntity<List<FileUploadDto>> getFilesForEntity(
            @Parameter(description = "Tipul entității (ex: time_off_request, product)", required = true)
            @RequestParam String entityType,
            @Parameter(description = "UUID-ul entității", required = true)
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
    @Operation(summary = "Șterge fișierele asociate unei entități",
            description = "Șterge toate fișierele legate de o entitate specificată prin tip și UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Fișierele au fost șterse cu succes"),
            @ApiResponse(responseCode = "400", description = "Parametri invalizi")
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteFilesForEntity(
            @Parameter(description = "Tipul entității (ex: time_off_request, product)", required = true)
            @RequestParam String entityType,
            @Parameter(description = "UUID-ul entității", required = true)
            @RequestParam UUID entityId
    ) {
        fileStorageService.deleteFilesForEntity(entityType, entityId);
        return ResponseEntity.noContent().build();
    }
}

