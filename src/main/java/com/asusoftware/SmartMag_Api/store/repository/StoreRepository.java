package com.asusoftware.SmartMag_Api.store.repository;

import com.asusoftware.SmartMag_Api.store.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {
    List<Store> findByCompanyId(UUID companyId);
}
