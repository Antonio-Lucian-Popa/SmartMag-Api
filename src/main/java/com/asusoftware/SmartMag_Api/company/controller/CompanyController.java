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

/**
 * Controller pentru operațiuni legate de compania utilizatorului (creare, citire, actualizare, ștergere).
 * Toate acțiunile sunt legate de utilizatorul logat (OWNER).
 */
@RestController
@RequestMapping("/api/v1/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    /**
     * Creează o companie nouă și setează utilizatorul curent ca OWNER.
     * Se apelează de obicei la înregistrare sau prima autentificare.
     *
     * @param dto obiect care conține informațiile necesare companiei (nume, descriere etc.)
     * @param principal Jwt token-ul userului logat
     * @return Obiectul CompanyDto cu detalii despre compania creată
     */
    @PostMapping
    public ResponseEntity<CompanyDto> createCompany(
            @Valid @RequestBody CreateCompanyDto dto,
            @AuthenticationPrincipal Jwt principal
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        CompanyDto companyDto = companyService.createCompany(dto, keycloakId);
        return ResponseEntity.ok(companyDto);
    }

    /**
     * Returnează compania asociată utilizatorului logat (OWNER sau membru al companiei).
     *
     * @param principal Jwt token-ul userului logat
     * @return Obiectul CompanyDto cu informațiile companiei curente
     */
    @GetMapping("/me")
    public ResponseEntity<CompanyDto> getMyCompany(@AuthenticationPrincipal Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(companyService.getMyCompany(keycloakId));
    }

    /**
     * Actualizează detaliile companiei curente (doar dacă utilizatorul este OWNER).
     *
     * @param dto noile informații despre companie
     * @param principal Jwt token-ul userului logat
     * @return Obiectul CompanyDto actualizat
     */
    @PutMapping
    public ResponseEntity<CompanyDto> updateCompany(
            @AuthenticationPrincipal Jwt principal,
            @Valid @RequestBody CreateCompanyDto dto
    ) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        return ResponseEntity.ok(companyService.updateCompany(keycloakId, dto));
    }

    /**
     * Șterge compania curentă (doar dacă utilizatorul este OWNER).
     * De obicei presupune și ștergerea în lanț a tuturor entităților asociate (store-uri, angajați etc.).
     *
     * @param principal Jwt token-ul userului logat
     * @return 204 No Content dacă ștergerea s-a realizat cu succes
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteCompany(@AuthenticationPrincipal Jwt principal) {
        UUID keycloakId = UUID.fromString(principal.getSubject());
        companyService.deleteCompany(keycloakId);
        return ResponseEntity.noContent().build();
    }
}


