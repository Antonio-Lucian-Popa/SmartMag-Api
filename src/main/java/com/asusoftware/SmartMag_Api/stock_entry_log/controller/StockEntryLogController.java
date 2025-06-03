package com.asusoftware.SmartMag_Api.stock_entry_log.controller;

import com.asusoftware.SmartMag_Api.stock_entry_log.model.dto.CreateStockEntryLogDto;
import com.asusoftware.SmartMag_Api.stock_entry_log.model.dto.StockEntryLogDto;
import com.asusoftware.SmartMag_Api.stock_entry_log.service.StockEntryLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Controller pentru gestionarea logurilor de intrări în stoc.
 * Permite înregistrarea manuală a modificărilor de stoc (intrări/ieșiri) și vizualizarea acestora.
 */
@RestController
@RequestMapping("/api/v1/stock-log")
@RequiredArgsConstructor
public class StockEntryLogController {

    private final StockEntryLogService stockEntryLogService;

    /**
     * Creează o înregistrare nouă în logul de stocuri.
     * Se poate folosi pentru a înregistra o ajustare manuală de tip "ENTRY" sau "EXIT".
     *
     * @param dto         Obiectul care conține detalii despre log (produs, magazin, tip, cantitate etc.)
     * @param principal   JWT-ul utilizatorului autentificat
     * @return Logul creat, sub formă de DTO
     */
    @PostMapping
    public ResponseEntity<StockEntryLogDto> create(
            @Valid @RequestBody CreateStockEntryLogDto dto,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(stockEntryLogService.create(dto, keycloakId));
    }

    /**
     * Returnează toate înregistrările din logul de stocuri, filtrate opțional după produs sau magazin.
     *
     * @param productId (optional) ID-ul produsului pentru filtrare
     * @param storeId   (optional) ID-ul magazinului pentru filtrare
     * @return Lista logurilor de stoc corespunzătoare
     */
    @GetMapping
    public ResponseEntity<List<StockEntryLogDto>> getLogs(
            @RequestParam(required = false) UUID productId,
            @RequestParam(required = false) UUID storeId
    ) {
        return ResponseEntity.ok(stockEntryLogService.getFilteredLogs(productId, storeId));
    }
}

