package com.asusoftware.SmartMag_Api.store.controller;

import com.asusoftware.SmartMag_Api.store.model.dto.CreateStoreDto;
import com.asusoftware.SmartMag_Api.store.model.dto.StoreDto;
import com.asusoftware.SmartMag_Api.store.model.dto.UpdateStoreDto;
import com.asusoftware.SmartMag_Api.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller pentru gestionarea magazinelor din cadrul unei companii.
 * Permite adăugarea, modificarea, ștergerea și listarea magazinelor deținute de compania utilizatorului curent.
 */
@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    /**
     * Creează un nou magazin pentru compania utilizatorului curent.
     *
     * @param dto        Datele noului magazin (nume, adresă etc.)
     * @param principal  JWT-ul utilizatorului autentificat
     * @return Magazinul creat sub formă de DTO
     */
    @PostMapping
    public ResponseEntity<StoreDto> create(
            @Valid @RequestBody CreateStoreDto dto,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(storeService.create(dto, keycloakId));
    }

    /**
     * Actualizează un magazin existent (doar dacă aparține companiei utilizatorului).
     *
     * @param id         ID-ul magazinului ce urmează a fi modificat
     * @param dto        Noile date ale magazinului
     * @param principal  JWT-ul utilizatorului autentificat
     * @return Magazinul actualizat
     */
    @PutMapping("/{id}")
    public ResponseEntity<StoreDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStoreDto dto,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(storeService.update(id, dto, keycloakId));
    }

    /**
     * Șterge un magazin (doar dacă aparține companiei utilizatorului).
     *
     * @param id         ID-ul magazinului de șters
     * @param principal  JWT-ul utilizatorului autentificat
     * @return 204 No Content dacă ștergerea a fost realizată cu succes
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        storeService.delete(id, keycloakId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Returnează lista magazinelor asociate companiei utilizatorului curent.
     *
     * @param principal JWT-ul utilizatorului autentificat
     * @return Lista magazinelor sub formă de DTO-uri
     */
    @GetMapping
    public ResponseEntity<List<StoreDto>> getAllByCompany(@AuthenticationPrincipal Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(storeService.getAllByCompany(keycloakId));
    }
}

