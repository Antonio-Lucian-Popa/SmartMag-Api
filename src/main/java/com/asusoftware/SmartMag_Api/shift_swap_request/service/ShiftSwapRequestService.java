package com.asusoftware.SmartMag_Api.shift_swap_request.service;

import com.asusoftware.SmartMag_Api.audit_log.service.AuditLogService;
import com.asusoftware.SmartMag_Api.exception.ResourceNotFoundException;
import com.asusoftware.SmartMag_Api.notification.service.NotificationService;
import com.asusoftware.SmartMag_Api.shift_swap_history.model.ShiftSwapHistory;
import com.asusoftware.SmartMag_Api.shift_swap_history.service.ShiftSwapHistoryService;
import com.asusoftware.SmartMag_Api.shift_swap_request.model.ShiftSwapRequest;
import com.asusoftware.SmartMag_Api.shift_swap_request.model.ShiftSwapStatus;
import com.asusoftware.SmartMag_Api.shift_swap_request.model.dto.CreateShiftSwapRequestDto;
import com.asusoftware.SmartMag_Api.shift_swap_request.model.dto.ShiftSwapRequestDto;
import com.asusoftware.SmartMag_Api.shift_swap_request.repository.ShiftSwapRequestRepository;
import com.asusoftware.SmartMag_Api.user.model.User;
import com.asusoftware.SmartMag_Api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftSwapRequestService {

    private final ShiftSwapRequestRepository shiftSwapRequestRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final ShiftSwapHistoryService shiftSwapHistoryService;
    private final AuditLogService auditLogService;
    private final ModelMapper mapper;

    @Transactional
    public ShiftSwapRequestDto createRequest(CreateShiftSwapRequestDto dto, UUID keycloakId) {
        User fromUser = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (dto.getDate() == null || dto.getShiftType() == null || dto.getStoreId() == null || dto.getToUserId() == null) {
            throw new IllegalArgumentException("All fields are required.");
        }

        if (fromUser.getId().equals(dto.getToUserId())) {
            throw new IllegalArgumentException("Cannot request shift swap with yourself.");
        }

        ShiftSwapRequest request = ShiftSwapRequest.builder()
                .fromUserId(fromUser.getId())
                .toUserId(dto.getToUserId())
                .storeId(dto.getStoreId())
                .date(dto.getDate())
                .shiftType(dto.getShiftType())
                .status(ShiftSwapStatus.PENDING)
                .build();

        ShiftSwapRequest shiftSwapRequest = shiftSwapRequestRepository.save(request);

        // Trimite notificare utilizatorului target
        notificationService.sendNotification(
                dto.getToUserId(),
                "Ai primit o solicitare de schimb de tură pentru data " + dto.getDate() + " de la: " + fromUser.getFirstName() + ".",
                "SHIFT_SWAP_REQUEST"
        );

        User toUser = userRepository.findById(dto.getToUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Target user not found"));

        // Logare audit
        auditLogService.log(fromUser.getId(), "CREATE_SHIFT_SWAP_REQUEST", "Solicitare către: " + toUser.getEmail() + " din partea " + fromUser.getEmail()
                + ", Data: " + dto.getDate() + ", Tipul turei: " + dto.getShiftType());

        return mapper.map(shiftSwapRequest, ShiftSwapRequestDto.class);
    }

    public List<ShiftSwapRequestDto> getMyRequests(UUID keycloakId) {
        UUID userId = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"))
                .getId();

        return shiftSwapRequestRepository.findAllByFromUserIdOrToUserId(userId, userId).stream()
                .map(r -> mapper.map(r, ShiftSwapRequestDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void acceptRequest(UUID requestId, UUID keycloakId) {
        respondToRequest(requestId, true, keycloakId);
    }

    @Transactional
    public void rejectRequest(UUID requestId, UUID keycloakId) {
        respondToRequest(requestId, false, keycloakId);
    }


    @Transactional
    public ShiftSwapRequestDto respondToRequest(UUID requestId, boolean approve, UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ShiftSwapRequest request = shiftSwapRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        if (!request.getToUserId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not authorized to respond to this request.");
        }

        request.setStatus(approve ? ShiftSwapStatus.APPROVED : ShiftSwapStatus.REJECTED);

        ShiftSwapRequest updatedRequest = shiftSwapRequestRepository.save(request);

        // Trimite notificare utilizatorului care a făcut solicitarea
        notificationService.sendNotification(
                request.getFromUserId(),
                "Solicitarea de schimb de tură pentru data " + request.getDate() + " a fost " + (approve ? "aprobată" : "respinsă") + ".",
                "SHIFT_SWAP_RESPONSE"
        );

        // Logare audit
        auditLogService.log(user.getId(), "UPDATE_SHIFT_SWAP_STATUS", "Request ID: " + requestId + ", Status: " + request.getStatus());

        ShiftSwapRequestDto shiftSwapRequestDto = mapper.map(updatedRequest, ShiftSwapRequestDto.class);

        CreateShiftSwapRequestDto createShiftSwapRequestDto = CreateShiftSwapRequestDto.builder()
                .fromUserId(shiftSwapRequestDto.getFromUserId())
                .toUserId(shiftSwapRequestDto.getToUserId())
                .storeId(shiftSwapRequestDto.getStoreId())
                .date(shiftSwapRequestDto.getDate())
                .shiftType(shiftSwapRequestDto.getShiftType())
                .status(shiftSwapRequestDto.getStatus())
                .build();

        // Salvare în ShiftSwapHistory
        shiftSwapHistoryService.createShiftSwapRequest(
                createShiftSwapRequestDto,
                keycloakId
        );

        return shiftSwapRequestDto;
    }
}

