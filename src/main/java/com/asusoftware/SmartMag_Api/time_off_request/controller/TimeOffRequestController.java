package com.asusoftware.SmartMag_Api.time_off_request.controller;

import com.asusoftware.SmartMag_Api.time_off_request.model.dto.CreateTimeOffRequestDto;
import com.asusoftware.SmartMag_Api.time_off_request.model.dto.TimeOffRequestDto;
import com.asusoftware.SmartMag_Api.time_off_request.service.TimeOffRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/time-off")
@RequiredArgsConstructor
public class TimeOffRequestController {

    private final TimeOffRequestService timeOffRequestService;

    @PostMapping
    public ResponseEntity<TimeOffRequestDto> requestTimeOff(
            @AuthenticationPrincipal Jwt principal,
            @Valid @RequestBody CreateTimeOffRequestDto dto
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(timeOffRequestService.createTimeOffRequest(dto, keycloakId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<TimeOffRequestDto>> getMyRequests(
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(timeOffRequestService.getMyRequests(keycloakId));
    }

    @GetMapping("/company")
    public ResponseEntity<List<TimeOffRequestDto>> getCompanyRequests(
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(timeOffRequestService.getRequestsForCompany(keycloakId));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approveRequest(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        timeOffRequestService.approveRequest(id, keycloakId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> rejectRequest(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        timeOffRequestService.rejectRequest(id, keycloakId);
        return ResponseEntity.noContent().build();
    }
}
