package com.asusoftware.SmartMag_Api.shift.controller;

import com.asusoftware.SmartMag_Api.shift.model.dto.CreateShiftDto;
import com.asusoftware.SmartMag_Api.shift.model.dto.ShiftDto;
import com.asusoftware.SmartMag_Api.shift.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping
    public ResponseEntity<ShiftDto> create(
            @Valid @RequestBody CreateShiftDto dto,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(shiftService.createShift(dto, keycloakId));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ShiftDto>> getByStore(
            @PathVariable UUID storeId
    ) {
        return ResponseEntity.ok(shiftService.getShiftsByStore(storeId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ShiftDto>> getMyShifts(
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(shiftService.getShiftsByUser(keycloakId));
    }
}