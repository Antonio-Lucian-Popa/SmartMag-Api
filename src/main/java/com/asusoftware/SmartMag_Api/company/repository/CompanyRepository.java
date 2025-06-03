package com.asusoftware.SmartMag_Api.company.repository;

import com.asusoftware.SmartMag_Api.company.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository pentru entitatea Company.
 * Permite operații CRUD și căutări customizate (ex: după CUI).
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    /**
     * Găsește o companie după CUI.
     *
     * @param cui Codul Unic de Înregistrare
     * @return Optional cu compania dacă există
     */
    Optional<Company> findByCui(String cui);
}

