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

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/check-in")
    public ResponseEntity<AttendanceDto> checkIn(
            @AuthenticationPrincipal Jwt principal,
            @Valid @RequestBody CheckInRequestDto dto
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(attendanceService.checkIn(keycloakId, dto.getStoreId()));
    }


    @PostMapping("/check-out")
    public ResponseEntity<AttendanceDto> checkOut(
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(attendanceService.checkOut(keycloakId));
    }

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
