package com.asusoftware.SmartMag_Api.shift_swap_history.repository;

import com.asusoftware.SmartMag_Api.shift_swap_history.model.ShiftSwapHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ShiftSwapHistoryRepository extends JpaRepository<ShiftSwapHistory, UUID> {
    List<ShiftSwapHistory> findAllByOldUserIdOrNewUserId(UUID oldUserId, UUID newUserId);
    List<ShiftSwapHistory> findAllByOldUserIdOrNewUserIdAndDateBetween(
            UUID oldUserId, UUID newUserId, LocalDate startDate, LocalDate endDate
    );
}

