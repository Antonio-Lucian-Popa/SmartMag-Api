package com.asusoftware.SmartMag_Api.attendance.controller;

import com.asusoftware.SmartMag_Api.attendance.model.dto.AttendanceDto;
import com.asusoftware.SmartMag_Api.attendance.model.dto.CheckInRequestDto;
import com.asusoftware.SmartMag_Api.attendance.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Controller pentru gestionarea pontajului (check-in, check-out, vizualizare pontaj personal).
 */
@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    /**
     * Permite unui utilizator să dea check-in într-un magazin.
     * Înregistrează ora de început a prezenței pentru ziua curentă.
     *
     * @param principal Jwt token-ul userului logat (folosit pentru a extrage keycloakId)
     * @param dto conține storeId (magazinul unde se face check-in)
     * @return Detalii despre prezența înregistrată
     */
    @PostMapping("/check-in")
    public ResponseEntity<AttendanceDto> checkIn(
            @AuthenticationPrincipal Jwt principal,
            @Valid @RequestBody CheckInRequestDto dto
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(attendanceService.checkIn(keycloakId, dto.getStoreId()));
    }

    /**
     * Permite unui utilizator să dea check-out.
     * Completează prezența curentă (dacă există) cu ora de sfârșit.
     *
     * @param principal Jwt token-ul userului logat
     * @return Detalii actualizate despre prezență
     */
    @PostMapping("/check-out")
    public ResponseEntity<AttendanceDto> checkOut(
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(attendanceService.checkOut(keycloakId));
    }

    /**
     * Returnează toate înregistrările de prezență ale utilizatorului curent.
     * Opțional, pot fi filtrate după interval de timp (start-end).
     *
     * @param principal Jwt token-ul userului logat
     * @param start data de început a filtrării (opțional)
     * @param end data de sfârșit a filtrării (opțional)
     * @return Lista de prezențe (AttendanceDto)
     */
    @GetMapping("/my")
    public ResponseEntity<List<AttendanceDto>> getMyAttendance(
            @AuthenticationPrincipal Jwt principal,
            @RequestParam(required = false) LocalDate start,
            @RequestParam(required = false) LocalDate end
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(attendanceService.getMyAttendance(keycloakId, start, end));
    }
}

