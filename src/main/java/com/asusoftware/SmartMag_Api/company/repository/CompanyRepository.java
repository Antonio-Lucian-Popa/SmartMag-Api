package com.asusoftware.SmartMag_Api.company.repository;

import com.asusoftware.SmartMag_Api.company.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByCui(String cui);
}

