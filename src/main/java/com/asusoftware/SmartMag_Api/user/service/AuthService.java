package com.asusoftware.SmartMag_Api.user.service;

import com.asusoftware.SmartMag_Api.config.KeycloakService;
import com.asusoftware.SmartMag_Api.user.model.User;
import com.asusoftware.SmartMag_Api.user.model.dto.LoginDto;
import com.asusoftware.SmartMag_Api.user.model.dto.UpdateUserProfileDto;
import com.asusoftware.SmartMag_Api.user.model.dto.UserDto;
import com.asusoftware.SmartMag_Api.user.model.dto.UserRegisterDto;
import com.asusoftware.SmartMag_Api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.modelmapper.ModelMapper;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KeycloakService keycloakService;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Transactional
    public UserDto register(UserRegisterDto dto) {
        UUID keycloakId = UUID.fromString(keycloakService.createKeycloakUser(dto));

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setKeycloakId(keycloakId);
        user.setCompanyId(null); // va fi setat mai târziu dacă e OWNER

        user = userRepository.save(user);
        return mapper.map(user, UserDto.class);
    }


    public AccessTokenResponse login(LoginDto dto) {
        return keycloakService.loginUser(dto);
    }

    public AccessTokenResponse refresh(String refreshToken) {
        return keycloakService.refreshToken(refreshToken);
    }

    public UserDto completeProfile(Jwt principal, UpdateUserProfileDto dto) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        return mapper.map(userRepository.save(user), UserDto.class);
    }

    public UserDto getCurrentUser(Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.map(user, UserDto.class);
    }
}

