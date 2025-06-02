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

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreDto> create(@Valid @RequestBody CreateStoreDto dto, @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(storeService.create(dto, keycloakId));
    }

    @PutMapping("/{id}")
            public ResponseEntity<StoreDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStoreDto dto,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(storeService.update(id, dto, keycloakId));
    }

    @DeleteMapping("/{id}")
            public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        storeService.delete(id, keycloakId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<StoreDto>> getAllByCompany(@AuthenticationPrincipal Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(storeService.getAllByCompany(keycloakId));
    }
}
