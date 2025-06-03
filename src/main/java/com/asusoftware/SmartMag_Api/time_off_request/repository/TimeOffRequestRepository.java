package com.asusoftware.SmartMag_Api.time_off_request.repository;

import com.asusoftware.SmartMag_Api.time_off_request.model.TimeOffRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TimeOffRequestRepository extends JpaRepository<TimeOffRequest, UUID> {

    List<TimeOffRequest> findByUserId(UUID userId);

    boolean existsByUserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            UUID userId, LocalDate endDate, LocalDate startDate
    );

    List<TimeOffRequest> findAllByCompanyId(UUID companyId);
}
