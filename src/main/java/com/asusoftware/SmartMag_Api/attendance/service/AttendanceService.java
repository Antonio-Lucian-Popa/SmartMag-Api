package com.asusoftware.SmartMag_Api.attendance.service;

import com.asusoftware.SmartMag_Api.attendance.model.Attendance;
import com.asusoftware.SmartMag_Api.attendance.model.dto.AttendanceDto;
import com.asusoftware.SmartMag_Api.attendance.model.dto.CreateAttendanceDto;
import com.asusoftware.SmartMag_Api.attendance.repository.AttendanceRepository;
import com.asusoftware.SmartMag_Api.exception.ResourceNotFoundException;
import com.asusoftware.SmartMag_Api.store.repository.UserStoreRepository;
import com.asusoftware.SmartMag_Api.user.model.User;
import com.asusoftware.SmartMag_Api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final UserStoreRepository userStoreRepository;

    public AttendanceDto checkIn(UUID keycloakId, UUID storeId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean alreadyCheckedIn = attendanceRepository
                .existsByUserIdAndCheckOutIsNull(user.getId());

        if (alreadyCheckedIn) {
            throw new IllegalStateException("You are already checked in and haven't checked out yet.");
        }

        if (!userStoreRepository.existsByUserIdAndStoreId(user.getId(), storeId)) {
            throw new AccessDeniedException("You are not assigned to this store.");
        }

        // TODO: Verifică dacă userul chiar e asociat cu acel storeId
        // ex: userStoreRepository.existsByUserIdAndStoreId(user.getId(), storeId)

        Attendance attendance = Attendance.builder()
                .userId(user.getId())
                .storeId(storeId)
                .checkIn(LocalDateTime.now())
                .build();

        attendance = attendanceRepository.save(attendance);
        return mapper.map(attendance, AttendanceDto.class);
    }


    public AttendanceDto checkOut(UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Attendance latestOpen = attendanceRepository
                .findTopByUserIdAndCheckOutIsNullOrderByCheckInDesc(user.getId())
                .orElseThrow(() -> new IllegalStateException("No check-in record found."));

        latestOpen.setCheckOut(LocalDateTime.now());
        attendanceRepository.save(latestOpen);
        return mapper.map(latestOpen, AttendanceDto.class);
    }

    public List<AttendanceDto> getMyAttendance(UUID keycloakId, LocalDate start, LocalDate end) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Attendance> records;

        if (start != null && end != null) {
            records = attendanceRepository.findByUserIdAndCheckInBetween(
                    user.getId(),
                    start.atStartOfDay(),
                    end.atTime(23, 59, 59)
            );
        } else {
            records = attendanceRepository.findByUserId(user.getId());
        }

        return records.stream()
                .map(a -> mapper.map(a, AttendanceDto.class))
                .collect(Collectors.toList());
    }
}

