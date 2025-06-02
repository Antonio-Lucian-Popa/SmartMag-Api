package com.asusoftware.SmartMag_Api.attendance.repository;

import com.asusoftware.SmartMag_Api.attendance.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    List<Attendance> findByUserId(UUID userId);

    List<Attendance> findByStoreId(UUID storeId);

    List<Attendance> findByUserIdAndDateBetween(UUID userId, LocalDate startDate, LocalDate endDate);

    Optional<Attendance> findByUserIdAndDate(UUID userId, LocalDate date);

    Optional<Attendance> findTopByUserIdAndCheckOutIsNullOrderByCheckInDesc(UUID userId);

    boolean existsByUserIdAndCheckOutIsNull(UUID userId);

    List<Attendance> findByUserIdAndCheckInBetween(UUID userId, LocalDateTime start, LocalDateTime end);

}
