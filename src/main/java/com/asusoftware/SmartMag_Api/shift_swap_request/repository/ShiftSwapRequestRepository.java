package com.asusoftware.SmartMag_Api.shift_swap_request.repository;

import com.asusoftware.SmartMag_Api.shift_swap_request.model.ShiftSwapRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShiftSwapRequestRepository extends JpaRepository<ShiftSwapRequest, UUID> {

    List<ShiftSwapRequest> findAllByFromUserIdOrToUserId(UUID fromUserId, UUID toUserId);
}

