package com.asusoftware.SmartMag_Api.user.controller;

import com.asusoftware.SmartMag_Api.user.model.dto.LoginDto;
import com.asusoftware.SmartMag_Api.user.model.dto.UpdateUserProfileDto;
import com.asusoftware.SmartMag_Api.user.model.dto.UserDto;
import com.asusoftware.SmartMag_Api.user.model.dto.UserRegisterDto;
import com.asusoftware.SmartMag_Api.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * Controller pentru autentificare și gestionarea conturilor utilizatorilor.
 * Include funcționalități de înregistrare, login, refresh token și completare profil.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Înregistrează un utilizator nou.
     * Creează utilizatorul în Keycloak și în baza de date locală.
     *
     * @param dto Obiect cu datele necesare pentru înregistrare (nume, email, parolă etc.)
     * @return Detalii despre utilizatorul creat
     */
    @Operation(summary = "Înregistrează un utilizator", description = "Creează un utilizator nou în Keycloak și returnează ID-ul acestuia.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilizator creat cu succes"),
            @ApiResponse(responseCode = "409", description = "Email deja folosit"),
            @ApiResponse(responseCode = "500", description = "Eroare internă")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserRegisterDto dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    /**
     * Autentifică utilizatorul și returnează un token de acces + refresh token.
     *
     * @param dto Obiect cu email și parolă
     * @return Obiect cu access token și refresh token
     */
    @Operation(summary = "Login utilizator", description = "Autentificare utilizator cu email și parolă. Returnează token JWT.")
    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@Valid @RequestBody LoginDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    /**
     * Reînnoiește access token-ul folosind un refresh token valid.
     *
     * @param refreshToken Tokenul de reîmprospătare
     * @return Un nou access token și refresh token
     */
    @Operation(summary = "Refresh token", description = "Reîmprospătare token folosind refresh token.")
    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refresh(@RequestParam String refreshToken) {
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }

    /**
     * Permite completarea profilului utilizatorului după login cu Google sau înregistrare parțială.
     * Actualizează datele lipsă precum nume, companie etc.
     *
     * @param dto       Obiect cu datele de completare a profilului
     * @param principal JWT-ul utilizatorului curent (autentificat)
     * @return Obiectul complet cu datele utilizatorului
     */
    @Operation(summary = "Completează profilul utilizatorului", description = "Actualizează datele lipsă ale utilizatorului după login cu Google sau înregistrare parțială.")
    @PostMapping("/complete-profile")
    public ResponseEntity<UserDto> completeProfile(
            @Valid @RequestBody UpdateUserProfileDto dto,
            @AuthenticationPrincipal Jwt principal
    ) {
        return ResponseEntity.ok(authService.completeProfile(principal, dto));
    }

    /**
     * Returnează datele utilizatorului curent autentificat (din baza locală).
     *
     * @param principal JWT-ul utilizatorului curent
     * @return Obiectul cu datele complete ale utilizatorului
     */
    @Operation(summary = "Obține utilizatorul curent", description = "Returnează datele utilizatorului curent autentificat.")
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal Jwt principal) {
        return ResponseEntity.ok(authService.getCurrentUser(principal));
    }
}

