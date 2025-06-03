package com.asusoftware.SmartMag_Api.shift_swap_history.service;

import com.asusoftware.SmartMag_Api.exception.ResourceNotFoundException;
import com.asusoftware.SmartMag_Api.notification.service.NotificationService;
import com.asusoftware.SmartMag_Api.shift_swap_history.model.ShiftSwapHistory;
import com.asusoftware.SmartMag_Api.shift_swap_history.model.dto.ShiftSwapHistoryDto;
import com.asusoftware.SmartMag_Api.shift_swap_history.repository.ShiftSwapHistoryRepository;
import com.asusoftware.SmartMag_Api.shift_swap_request.model.ShiftSwapRequest;
import com.asusoftware.SmartMag_Api.shift_swap_request.model.ShiftSwapStatus;
import com.asusoftware.SmartMag_Api.shift_swap_request.model.dto.CreateShiftSwapRequestDto;
import com.asusoftware.SmartMag_Api.shift_swap_request.model.dto.ShiftSwapRequestDto;
import com.asusoftware.SmartMag_Api.shift_swap_request.repository.ShiftSwapRequestRepository;
import com.asusoftware.SmartMag_Api.user.model.User;
import com.asusoftware.SmartMag_Api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftSwapHistoryService {

    private final ShiftSwapRequestRepository repository;
    private final ShiftSwapHistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final ModelMapper mapper;

    @Transactional
    public ShiftSwapRequestDto createShiftSwapRequest(CreateShiftSwapRequestDto dto, UUID keycloakId) {
        User fromUser = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        User toUser = userRepository.findById(dto.getToUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Target user not found"));

        ShiftSwapRequest request = ShiftSwapRequest.builder()
                .fromUserId(fromUser.getId())
                .toUserId(toUser.getId())
                .storeId(dto.getStoreId())
                .date(dto.getDate())
                .shiftType(dto.getShiftType())
                .status(ShiftSwapStatus.PENDING)
                .build();

        repository.save(request);

        notificationService.sendNotification(
                toUser.getId(),
                "Ai primit o solicitare de schimb de tură pentru data " + dto.getDate() + ".",
                "SHIFT_SWAP_REQUEST"
        );

        return mapper.map(request, ShiftSwapRequestDto.class);
    }

    public List<ShiftSwapRequestDto> getMyRequests(UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return repository.findAllByFromUserIdOrToUserId(user.getId(), user.getId()).stream()
                .map(r -> mapper.map(r, ShiftSwapRequestDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateRequestStatus(UUID requestId, ShiftSwapStatus status, UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ShiftSwapRequest request = repository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));

        if (!request.getToUserId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to respond to this request");
        }

        request.setStatus(status);
        repository.save(request);

        String message = status == ShiftSwapStatus.APPROVED
                ? "Cererea ta de schimb de tură a fost acceptată."
                : "Cererea ta de schimb de tură a fost refuzată.";

        String type = status == ShiftSwapStatus.APPROVED
                ? "SHIFT_SWAP_APPROVED"
                : "SHIFT_SWAP_REJECTED";

        notificationService.sendNotification(
                request.getFromUserId(),
                message,
                type
        );

        ShiftSwapHistory history = ShiftSwapHistory.builder()
                .oldUserId(request.getFromUserId())
                .newUserId(request.getToUserId())
                .storeId(request.getStoreId())
                .date(request.getDate())
                .shiftType(request.getShiftType())
                .approvedBy(user.getId())
                .build();

        historyRepository.save(history);
    }

    public List<ShiftSwapHistoryDto> getMySwapHistory(UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return historyRepository.findAllByOldUserIdOrNewUserId(user.getId(), user.getId())
                .stream()
                .map(history -> mapper.map(history, ShiftSwapHistoryDto.class))
                .collect(Collectors.toList());
    }

}
