package com.asusoftware.SmartMag_Api.shift.repository;

import com.asusoftware.SmartMag_Api.shift.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, UUID> {
    List<Shift> findByStoreId(UUID storeId);

    List<Shift> findByUserId(UUID userId);

    List<Shift> findByStoreIdAndStartTimeBetween(UUID storeId, LocalDateTime start, LocalDateTime end);

    boolean existsByUserIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            UUID userId, LocalDateTime end, LocalDateTime start
    );
}
