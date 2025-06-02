package com.asusoftware.SmartMag_Api.user.repository;


import com.asusoftware.SmartMag_Api.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByKeycloakId(UUID keycloakId);

    boolean existsByEmail(String email);

    List<User> findAllByCompanyId(UUID companyId);

}
