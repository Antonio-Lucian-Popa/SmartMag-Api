package com.asusoftware.SmartMag_Api.shift_swap_history.controller;

import com.asusoftware.SmartMag_Api.shift_swap_history.model.dto.ShiftSwapHistoryDto;
import com.asusoftware.SmartMag_Api.shift_swap_history.service.ShiftSwapHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shift-swap-history")
@RequiredArgsConstructor
public class ShiftSwapHistoryController {

    private final ShiftSwapHistoryService shiftSwapHistoryService;

    /**
     * Returnează istoricul schimburilor de ture pentru utilizatorul logat,
     * cu filtrare opțională după dată și magazin.
     */
    @GetMapping("/my")
    public ResponseEntity<List<ShiftSwapHistoryDto>> getMyShiftSwapHistory(
            @AuthenticationPrincipal Jwt principal,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) UUID storeId
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(
                shiftSwapHistoryService.getMyShiftSwapHistoryFiltered(keycloakId, startDate, endDate, storeId)
        );
    }
}
