package com.asusoftware.SmartMag_Api.store.service;

import com.asusoftware.SmartMag_Api.exception.ResourceNotFoundException;
import com.asusoftware.SmartMag_Api.exception.UserNotFoundException;
import com.asusoftware.SmartMag_Api.store.model.Store;
import com.asusoftware.SmartMag_Api.store.model.dto.CreateStoreDto;
import com.asusoftware.SmartMag_Api.store.model.dto.StoreDto;
import com.asusoftware.SmartMag_Api.store.model.dto.UpdateStoreDto;
import com.asusoftware.SmartMag_Api.store.repository.StoreRepository;
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
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public StoreDto create(CreateStoreDto dto, UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getCompanyId().equals(dto.getCompanyId())) {
            throw new IllegalArgumentException("You can only create stores in your own company");
        }

        Store store = Store.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .companyId(dto.getCompanyId())
                .build();

        return mapper.map(storeRepository.save(store), StoreDto.class);
    }

    public StoreDto update(UUID storeId, UpdateStoreDto dto, UUID keycloakId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));

                        User user = userRepository.findByKeycloakId(keycloakId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getCompanyId().equals(store.getCompanyId())) {
            throw new IllegalArgumentException("You can't update a store from another company");
        }

        store.setName(dto.getName());
        store.setAddress(dto.getAddress());

        return mapper.map(storeRepository.save(store), StoreDto.class);
    }

    public void delete(UUID storeId, UUID keycloakId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));

                        User user = userRepository.findByKeycloakId(keycloakId)
                        .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getCompanyId().equals(store.getCompanyId())) {
            throw new IllegalArgumentException("You can't delete a store from another company");
        }

        storeRepository.delete(store);
    }

    public List<StoreDto> getAllByCompany(UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return storeRepository.findByCompanyId(user.getCompanyId())
                .stream()
                .map(store -> mapper.map(store, StoreDto.class))
                .collect(Collectors.toList());
    }
}
