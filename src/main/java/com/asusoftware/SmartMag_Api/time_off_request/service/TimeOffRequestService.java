package com.asusoftware.SmartMag_Api.time_off_request.service;

import com.asusoftware.SmartMag_Api.audit_log.service.AuditLogService;
import com.asusoftware.SmartMag_Api.exception.ResourceNotFoundException;
import com.asusoftware.SmartMag_Api.notification.service.NotificationService;
import com.asusoftware.SmartMag_Api.time_off_request.model.TimeOffRequest;
import com.asusoftware.SmartMag_Api.time_off_request.model.TimeOffRequestStatus;
import com.asusoftware.SmartMag_Api.time_off_request.model.dto.CreateTimeOffRequestDto;
import com.asusoftware.SmartMag_Api.time_off_request.model.dto.TimeOffRequestDto;
import com.asusoftware.SmartMag_Api.time_off_request.repository.TimeOffRequestRepository;
import com.asusoftware.SmartMag_Api.user.model.User;
import com.asusoftware.SmartMag_Api.user.model.UserRole;
import com.asusoftware.SmartMag_Api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeOffRequestService {

    private final TimeOffRequestRepository timeOffRequestRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final AuditLogService auditLogService;
    private final ModelMapper mapper;

    @Transactional
    public TimeOffRequestDto createTimeOffRequest(CreateTimeOffRequestDto dto, UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Validare: datele să fie corecte
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        // Verificare dacă există deja cerere pe perioada aceea
        boolean overlapping = timeOffRequestRepository.existsByUserIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                user.getId(), dto.getEndDate(), dto.getStartDate()
        );
        if (overlapping) {
            throw new IllegalArgumentException("There is already a time off request in this period");
        }

        TimeOffRequest request = TimeOffRequest.builder()
                .userId(user.getId())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .type(dto.getType())
                .reason(dto.getReason())
                .status(TimeOffRequestStatus.PENDING)
                .build();

        request = timeOffRequestRepository.save(request);
        return mapper.map(request, TimeOffRequestDto.class);
    }

    public List<TimeOffRequestDto> getMyRequests(UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return timeOffRequestRepository.findByUserId(user.getId()).stream()
                .map(r -> mapper.map(r, TimeOffRequestDto.class))
                .collect(Collectors.toList());
    }

    public List<TimeOffRequestDto> getRequestsForCompany(UUID keycloakId) {
        User admin = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return timeOffRequestRepository.findAllByCompanyId(admin.getCompanyId()).stream()
                .map(r -> mapper.map(r, TimeOffRequestDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void approveRequest(UUID requestId, UUID keycloakId) {
        updateStatus(requestId, TimeOffRequestStatus.APPROVED, keycloakId);
        auditLogService.log(keycloakId, "TIME_OFF_APPROVED", "Cerere concediu aprobată: " + requestId);
    }

    @Transactional
    public void rejectRequest(UUID requestId, UUID keycloakId) {
        updateStatus(requestId, TimeOffRequestStatus.REJECTED, keycloakId);
        auditLogService.log(keycloakId, "TIME_OFF_REJECTED", "Cerere concediu a fost respinsa: " + requestId);
    }


    @Transactional
    public TimeOffRequestDto updateStatus(UUID requestId, TimeOffRequestStatus status, UUID keycloakId) {
        User admin = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TimeOffRequest request = timeOffRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        if (!admin.getCompanyId().equals(getUserCompanyId(request.getUserId()))) {
            throw new IllegalArgumentException("Access denied to this request");
        }

        if (!EnumSet.of(UserRole.OWNER, UserRole.MANAGER).contains(admin.getRole())) {
            throw new AccessDeniedException("You are not allowed to approve/reject time off requests");
        }

        request.setStatus(status);

        TimeOffRequest saved = timeOffRequestRepository.save(request);

        // Notificare automată
        String message = switch (status) {
            case APPROVED -> "Cererea ta de concediu a fost aprobată!";
            case REJECTED -> "Cererea ta de concediu a fost respinsă.";
            default -> null;
        };

        String type = switch (status) {
            case APPROVED -> "TIME_OFF_APPROVED";
            case REJECTED -> "TIME_OFF_REJECTED";
            default -> null;
        };

        if (message != null && type != null) {
            notificationService.sendNotification(request.getUserId(), message, type);
        }

        return mapper.map(saved, TimeOffRequestDto.class);
    }

    private UUID getUserCompanyId(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                .getCompanyId();
    }
}
