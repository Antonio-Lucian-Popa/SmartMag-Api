package com.asusoftware.SmartMag_Api.company.controller;

import com.asusoftware.SmartMag_Api.company.model.dto.CompanyDto;
import com.asusoftware.SmartMag_Api.company.model.dto.CreateCompanyDto;
import com.asusoftware.SmartMag_Api.company.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<CompanyDto> createCompany(
            @Valid @RequestBody CreateCompanyDto dto,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        CompanyDto companyDto = companyService.createCompany(dto, keycloakId);
        return ResponseEntity.ok(companyDto);
    }

    @GetMapping("/me")
    public ResponseEntity<CompanyDto> getMyCompany(@AuthenticationPrincipal Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(companyService.getMyCompany(keycloakId));
    }

    @PutMapping
    public ResponseEntity<CompanyDto> updateCompany(
            @AuthenticationPrincipal Jwt principal,
            @Valid @RequestBody CreateCompanyDto dto
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(companyService.updateCompany(keycloakId, dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCompany(@AuthenticationPrincipal Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        companyService.deleteCompany(keycloakId);
        return ResponseEntity.noContent().build();
    }

}

