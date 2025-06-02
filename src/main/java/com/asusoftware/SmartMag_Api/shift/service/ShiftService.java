package com.asusoftware.SmartMag_Api.shift.service;

import com.asusoftware.SmartMag_Api.exception.ResourceNotFoundException;
import com.asusoftware.SmartMag_Api.shift.model.Shift;
import com.asusoftware.SmartMag_Api.shift.model.dto.CreateShiftDto;
import com.asusoftware.SmartMag_Api.shift.model.dto.ShiftDto;
import com.asusoftware.SmartMag_Api.shift.repository.ShiftRepository;
import com.asusoftware.SmartMag_Api.user.model.User;
import com.asusoftware.SmartMag_Api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public ShiftDto createShift(CreateShiftDto dto, UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Shift shift = mapper.map(dto, Shift.class);
        shift.setCompanyId(user.getCompanyId());
        shift = shiftRepository.save(shift);
        return mapper.map(shift, ShiftDto.class);
    }

    public List<ShiftDto> getShiftsByStore(UUID storeId) {
        return shiftRepository.findByStoreId(storeId).stream()
                .map(s -> mapper.map(s, ShiftDto.class))
                .collect(Collectors.toList());
    }


    public List<ShiftDto> getShiftsForStore(UUID storeId) {
        return shiftRepository.findByStoreId(storeId).stream()
                .map(s -> mapper.map(s, ShiftDto.class))
                .collect(Collectors.toList());
    }

    public List<ShiftDto> getShiftsByUser(UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return shiftRepository.findByUserId(user.getId()).stream()
                .map(s -> mapper.map(s, ShiftDto.class))
                .collect(Collectors.toList());
    }

}
