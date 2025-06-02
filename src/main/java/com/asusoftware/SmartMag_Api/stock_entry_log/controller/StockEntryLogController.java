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

@RestController
@RequestMapping("/api/v1/stock-log")
@RequiredArgsConstructor
public class StockEntryLogController {

    private final StockEntryLogService stockEntryLogService;

    @PostMapping
    public ResponseEntity<StockEntryLogDto> create(
            @Valid @RequestBody CreateStockEntryLogDto dto,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(stockEntryLogService.create(dto, keycloakId));
    }

    @GetMapping
    public ResponseEntity<List<StockEntryLogDto>> getLogs(
            @RequestParam(required = false) UUID productId,
            @RequestParam(required = false) UUID storeId
    ) {
        return ResponseEntity.ok(stockEntryLogService.getFilteredLogs(productId, storeId));
    }
}
