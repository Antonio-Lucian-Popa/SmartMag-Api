package com.asusoftware.SmartMag_Api.shift_swap_request.controller;

import com.asusoftware.SmartMag_Api.shift_swap_request.model.dto.CreateShiftSwapRequestDto;
import com.asusoftware.SmartMag_Api.shift_swap_request.model.dto.ShiftSwapRequestDto;
import com.asusoftware.SmartMag_Api.shift_swap_request.service.ShiftSwapRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller REST pentru gestionarea cererilor de schimb de tură.
 * Permite angajaților să trimită cereri de schimb de tură către colegi și să accepte/respingă cererile primite.
 */
@RestController
@RequestMapping("/api/v1/shift-swap")
@RequiredArgsConstructor
public class ShiftSwapRequestController {

    private final ShiftSwapRequestService shiftSwapRequestService;

    /**
     * Creează o cerere nouă de schimb de tură.
     * Userul curent (autentificat) este cel care inițiază cererea către un alt angajat.
     *
     * @param principal Jwt token-ul userului curent
     * @param dto obiect cu informațiile cererii (data, store, user de schimb, tip tură)
     * @return cererea creată (DTO)
     */
    @PostMapping
    public ResponseEntity<ShiftSwapRequestDto> createSwapRequest(
            @AuthenticationPrincipal Jwt principal,
            @Valid @RequestBody CreateShiftSwapRequestDto dto
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(shiftSwapRequestService.createRequest(dto, keycloakId));
    }

    /**
     * Returnează toate cererile de schimb de tură în care userul este implicat,
     * fie ca inițiator (from_user), fie ca destinatar (to_user).
     *
     * @param principal Jwt token-ul userului curent
     * @return lista cererilor în care userul este implicat
     */
    @GetMapping("/my")
    public ResponseEntity<List<ShiftSwapRequestDto>> getMySwapRequests(
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(shiftSwapRequestService.getMyRequests(keycloakId));
    }

    /**
     * Acceptă o cerere de schimb de tură (doar destinatarul cererii poate face acest lucru).
     *
     * @param id ID-ul cererii
     * @param principal Jwt token-ul userului curent
     * @return 204 No Content dacă operația s-a realizat cu succes
     */
    @PostMapping("/{id}/accept")
    public ResponseEntity<Void> acceptRequest(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        shiftSwapRequestService.acceptRequest(id, keycloakId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Respinge o cerere de schimb de tură (doar destinatarul cererii poate face acest lucru).
     *
     * @param id ID-ul cererii
     * @param principal Jwt token-ul userului curent
     * @return 204 No Content dacă operația s-a realizat cu succes
     */
    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> rejectRequest(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        shiftSwapRequestService.rejectRequest(id, keycloakId);
        return ResponseEntity.noContent().build();
    }
}
