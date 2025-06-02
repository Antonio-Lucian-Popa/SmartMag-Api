package com.asusoftware.SmartMag_Api.user.controller;

import com.asusoftware.SmartMag_Api.user.model.dto.LoginDto;
import com.asusoftware.SmartMag_Api.user.model.dto.UpdateUserProfileDto;
import com.asusoftware.SmartMag_Api.user.model.dto.UserDto;
import com.asusoftware.SmartMag_Api.user.model.dto.UserRegisterDto;
import com.asusoftware.SmartMag_Api.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserRegisterDto dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@Valid @RequestBody LoginDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessTokenResponse> refresh(@RequestParam String refreshToken) {
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }

    @PostMapping("/complete-profile")
    public ResponseEntity<UserDto> completeProfile(
            @Valid @RequestBody UpdateUserProfileDto dto,
            @AuthenticationPrincipal Jwt principal
    ) {
        return ResponseEntity.ok(authService.completeProfile(principal, dto));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal Jwt principal) {
        return ResponseEntity.ok(authService.getCurrentUser(principal));
    }
}
