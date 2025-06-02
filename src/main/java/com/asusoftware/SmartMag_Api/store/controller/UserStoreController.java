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

@RestController
@RequestMapping("/api/v1/user-store")
@RequiredArgsConstructor
public class UserStoreController {

    private final UserStoreService userStoreService;

    @PostMapping("/assign")
    public ResponseEntity<Void> assignUserToStore(
            @RequestBody AssignUserToStoreDto dto,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        userStoreService.assignUserToStore(dto, keycloakId);
        return ResponseEntity.ok().build();
    }

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

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserStore>> getStoresForUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(userStoreService.getStoresForUser(userId));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<UserStore>> getUsersForStore(@PathVariable UUID storeId) {
        return ResponseEntity.ok(userStoreService.getUsersForStore(storeId));
    }
}
