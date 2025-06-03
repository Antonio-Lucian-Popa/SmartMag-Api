package com.asusoftware.SmartMag_Api.store.controller;

import com.asusoftware.SmartMag_Api.store.model.UserStore;
import com.asusoftware.SmartMag_Api.store.model.dto.AssignUserToStoreDto;
import com.asusoftware.SmartMag_Api.store.service.UserStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller pentru gestionarea relației dintre utilizatori și magazine.
 * Permite asignarea și eliminarea utilizatorilor din magazine, precum și listarea asocierilor existente.
 */
@RestController
@RequestMapping("/api/v1/user-store")
@RequiredArgsConstructor
public class UserStoreController {

    private final UserStoreService userStoreService;

    /**
     * Asignează un utilizator la un magazin (de exemplu, un angajat este repartizat la un anumit punct de lucru).
     * Doar utilizatorii cu rol de admin/owner pot face această operație.
     *
     * @param dto        Obiectul care conține ID-ul utilizatorului și ID-ul magazinului
     * @param principal  JWT-ul utilizatorului autentificat
     * @return 200 OK dacă asocierea a fost realizată cu succes
     */
    @PostMapping("/assign")
    public ResponseEntity<Void> assignUserToStore(
            @RequestBody AssignUserToStoreDto dto,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        userStoreService.assignUserToStore(dto, keycloakId);
        return ResponseEntity.ok().build();
    }

    /**
     * Elimină un utilizator dintr-un magazin (șterge asocierea dintre user și store).
     * Doar adminii pot face acest lucru pentru utilizatorii companiei.
     *
     * @param userId     ID-ul utilizatorului de eliminat
     * @param storeId    ID-ul magazinului din care este eliminat utilizatorul
     * @param principal  JWT-ul utilizatorului autentificat
     * @return 204 No Content dacă operația a fost realizată
     */
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeUserFromStore(
            @RequestParam UUID userId,
            @RequestParam UUID storeId,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        userStoreService.removeUserFromStore(userId, storeId, keycloakId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Returnează toate magazinele la care este repartizat un anumit utilizator.
     * Util pentru admini sau pentru vizualizarea punctelor de lucru asociate unui angajat.
     *
     * @param userId ID-ul utilizatorului
     * @return Lista de asocieri (UserStore) pentru acel user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserStore>> getStoresForUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(userStoreService.getStoresForUser(userId));
    }

    /**
     * Returnează toți utilizatorii repartizați la un anumit magazin.
     * Util pentru a vedea cine lucrează într-un punct de lucru.
     *
     * @param storeId ID-ul magazinului
     * @return Lista de asocieri (UserStore) pentru acel magazin
     */
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<UserStore>> getUsersForStore(@PathVariable UUID storeId) {
        return ResponseEntity.ok(userStoreService.getUsersForStore(storeId));
    }
}

