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

/**
 * Controller pentru gestionarea cererilor de concediu/invoire.
 * Permite utilizatorilor să solicite timp liber și permite superiorilor să aprobe sau să respingă cererile.
 */
@RestController
@RequestMapping("/api/v1/time-off")
@RequiredArgsConstructor
public class TimeOffRequestController {

    private final TimeOffRequestService timeOffRequestService;

    /**
     * Creează o cerere de timp liber (concediu, învoire etc.) pentru utilizatorul curent.
     *
     * @param principal JWT-ul utilizatorului autentificat
     * @param dto       Datele cererii: interval, tip concediu etc.
     * @return Cererea creată sub formă de DTO
     */
    @PostMapping
    public ResponseEntity<TimeOffRequestDto> requestTimeOff(
            @AuthenticationPrincipal Jwt principal,
            @Valid @RequestBody CreateTimeOffRequestDto dto
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(timeOffRequestService.createTimeOffRequest(dto, keycloakId));
    }

    /**
     * Returnează toate cererile de concediu/invoire făcute de utilizatorul curent.
     *
     * @param principal JWT-ul utilizatorului
     * @return Lista de cereri proprii
     */
    @GetMapping("/my")
    public ResponseEntity<List<TimeOffRequestDto>> getMyRequests(
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(timeOffRequestService.getMyRequests(keycloakId));
    }

    /**
     * Returnează toate cererile de concediu pentru compania utilizatorului.
     * Vizibil doar pentru utilizatorii cu roluri de tip OWNER sau MANAGER.
     *
     * @param principal JWT-ul utilizatorului
     * @return Lista cererilor companiei
     */
    @GetMapping("/company")
    public ResponseEntity<List<TimeOffRequestDto>> getCompanyRequests(
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(timeOffRequestService.getRequestsForCompany(keycloakId));
    }

    /**
     * Aprobă o cerere de timp liber. Doar superiorii pot aproba cererile.
     *
     * @param id        ID-ul cererii
     * @param principal JWT-ul utilizatorului curent
     * @return 204 No Content dacă cererea a fost aprobată
     */
    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approveRequest(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        timeOffRequestService.approveRequest(id, keycloakId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Respinge o cerere de timp liber. Doar superiorii pot face acest lucru.
     *
     * @param id        ID-ul cererii
     * @param principal JWT-ul utilizatorului curent
     * @return 204 No Content dacă cererea a fost respinsă
     */
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

