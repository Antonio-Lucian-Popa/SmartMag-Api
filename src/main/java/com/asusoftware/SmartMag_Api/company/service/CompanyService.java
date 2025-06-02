package com.asusoftware.SmartMag_Api.company.service;

import com.asusoftware.SmartMag_Api.company.model.Company;
import com.asusoftware.SmartMag_Api.company.model.dto.CompanyDto;
import com.asusoftware.SmartMag_Api.company.model.dto.CreateCompanyDto;
import com.asusoftware.SmartMag_Api.company.repository.CompanyRepository;
import com.asusoftware.SmartMag_Api.user.model.User;
import com.asusoftware.SmartMag_Api.user.model.UserRole;
import com.asusoftware.SmartMag_Api.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Transactional
    public CompanyDto createCompany(CreateCompanyDto dto, UUID keycloakId) {
        // Verifică dacă există deja companie cu același CUI
        companyRepository.findByCui(dto.getCui()).ifPresent(c -> {
            throw new IllegalArgumentException("CUI-ul este deja folosit de altă companie.");
        });

        // Găsește userul care creează compania
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UsernameNotFoundException("Userul nu există."));

        if (user.getRole() != UserRole.OWNER) {
            throw new AccessDeniedException("Doar utilizatorii cu rol OWNER pot crea companie.");
        }

        if (user.getCompanyId() != null) {
            throw new IllegalStateException("Utilizatorul are deja o companie.");
        }

        // Creează compania
        Company company = new Company();
        company.setName(dto.getName());
        company.setCui(dto.getCui());

        company = companyRepository.save(company);

        // Leagă userul de companie
        user.setCompanyId(company.getId());
        userRepository.save(user);

        return mapper.map(company, CompanyDto.class);
    }

    public CompanyDto getMyCompany(UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UsernameNotFoundException("Userul nu există."));

        if (user.getCompanyId() == null) {
            throw new IllegalStateException("Userul nu este asociat cu nicio companie.");
        }

        Company company = companyRepository.findById(user.getCompanyId())
                .orElseThrow(() -> new EntityNotFoundException("Compania nu a fost găsită."));

        return mapper.map(company, CompanyDto.class);
    }

    @Transactional
    public CompanyDto updateCompany(UUID keycloakId, CreateCompanyDto dto) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UsernameNotFoundException("Userul nu există."));

        if (user.getRole() != UserRole.OWNER || user.getCompanyId() == null) {
            throw new AccessDeniedException("Doar OWNER-ul poate actualiza compania.");
        }

        Company company = companyRepository.findById(user.getCompanyId())
                .orElseThrow(() -> new EntityNotFoundException("Compania nu a fost găsită."));

        // 🛡️ Verificare că userul editează DOAR compania lui
        if (!company.getId().equals(user.getCompanyId())) {
            throw new AccessDeniedException("Nu ai voie să modifici această companie.");
        }

        // Verificare CUI unic dacă s-a schimbat
        if (!company.getCui().equals(dto.getCui())) {
            companyRepository.findByCui(dto.getCui()).ifPresent(existing -> {
                throw new IllegalArgumentException("CUI-ul este deja folosit.");
            });
        }

        company.setName(dto.getName());
        company.setCui(dto.getCui());

        return mapper.map(companyRepository.save(company), CompanyDto.class);
    }


    @Transactional
    public void deleteCompany(UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UsernameNotFoundException("Userul nu există."));

        if (user.getRole() != UserRole.OWNER || user.getCompanyId() == null) {
            throw new AccessDeniedException("Doar OWNER-ul poate șterge compania.");
        }

        Company company = companyRepository.findById(user.getCompanyId())
                .orElseThrow(() -> new EntityNotFoundException("Compania nu a fost găsită."));

        // 🛡️ Confirmare că e compania lui
        if (!company.getId().equals(user.getCompanyId())) {
            throw new AccessDeniedException("Nu ai voie să ștergi această companie.");
        }

        List<User> users = userRepository.findAllByCompanyId(company.getId());
        if (users.size() > 1) {
            throw new IllegalStateException("Nu poți șterge compania cât timp sunt alți utilizatori asociați.");
        }

        companyRepository.deleteById(company.getId());
        user.setCompanyId(null);
        userRepository.save(user);
    }
}

