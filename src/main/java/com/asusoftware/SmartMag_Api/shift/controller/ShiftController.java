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

/**
 * Controller pentru gestionarea turelor de lucru (shifts).
 * Permite crearea unei ture, listarea turelor pentru un magazin și afișarea turelor proprii ale unui utilizator.
 */
@RestController
@RequestMapping("/api/v1/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    /**
     * Creează o tură nouă pentru un utilizator într-un anumit magazin.
     * Doar utilizatorii autorizați (ex: MANAGER, OWNER) pot crea ture.
     *
     * @param dto         Obiectul cu datele turei (data, oră, utilizator, magazin etc.)
     * @param principal   JWT-ul autentificat (identifică utilizatorul care face cererea)
     * @return Tura creată, sub formă de DTO
     */
    @PostMapping
    public ResponseEntity<ShiftDto> create(
            @Valid @RequestBody CreateShiftDto dto,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(shiftService.createShift(dto, keycloakId));
    }

    /**
     * Returnează toate turele programate într-un anumit magazin.
     * Util pentru afișarea programului pe magazin.
     *
     * @param storeId ID-ul magazinului
     * @return Lista de ture din magazinul respectiv
     */
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ShiftDto>> getByStore(
            @PathVariable UUID storeId
    ) {
        return ResponseEntity.ok(shiftService.getShiftsByStore(storeId));
    }

    /**
     * Returnează toate turele alocate utilizatorului curent (autentificat).
     * Util pentru ca angajatul să-și vadă programul.
     *
     * @param principal JWT-ul autentificat (pentru extragerea ID-ului utilizatorului)
     * @return Lista de ture proprii
     */
    @GetMapping("/my")
    public ResponseEntity<List<ShiftDto>> getMyShifts(
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(shiftService.getShiftsByUser(keycloakId));
    }
}
