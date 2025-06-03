package com.asusoftware.SmartMag_Api.shift_swap_request.service;

import com.asusoftware.SmartMag_Api.exception.ResourceNotFoundException;
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

        return mapper.map(shiftSwapRequestRepository.save(request), ShiftSwapRequestDto.class);
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
        return mapper.map(shiftSwapRequestRepository.save(request), ShiftSwapRequestDto.class);
    }
}

