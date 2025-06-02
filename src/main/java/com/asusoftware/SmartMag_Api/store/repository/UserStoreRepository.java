package com.asusoftware.SmartMag_Api.store.repository;

import com.asusoftware.SmartMag_Api.store.model.UserStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserStoreRepository extends JpaRepository<UserStore, UUID> {
    List<UserStore> findByStoreId(UUID storeId);
    List<UserStore> findByUserId(UUID userId);
    Optional<UserStore> findByUserIdAndStoreId(UUID userId, UUID storeId);
    void deleteByUserIdAndStoreId(UUID userId, UUID storeId);

    boolean existsByUserIdAndStoreId(UUID userId, UUID storeId);
}
