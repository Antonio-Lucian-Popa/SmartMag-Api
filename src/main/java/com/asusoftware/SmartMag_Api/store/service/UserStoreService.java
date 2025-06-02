package com.asusoftware.SmartMag_Api.store.service;

import com.asusoftware.SmartMag_Api.exception.ResourceNotFoundException;
import com.asusoftware.SmartMag_Api.store.model.Store;
import com.asusoftware.SmartMag_Api.store.model.UserStore;
import com.asusoftware.SmartMag_Api.store.model.dto.AssignUserToStoreDto;
import com.asusoftware.SmartMag_Api.store.repository.StoreRepository;
import com.asusoftware.SmartMag_Api.store.repository.UserStoreRepository;
import com.asusoftware.SmartMag_Api.user.model.User;
import com.asusoftware.SmartMag_Api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStoreService {

    private final UserStoreRepository userStoreRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public void assignUserToStore(AssignUserToStoreDto dto, UUID keycloakId) {
        User currentUser = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Store store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));

        if (!currentUser.getCompanyId().equals(user.getCompanyId()) ||
                !currentUser.getCompanyId().equals(store.getCompanyId())) {
            throw new IllegalArgumentException("Store and user must belong to the same company as the owner");
        }

        boolean alreadyAssigned = userStoreRepository.findByUserIdAndStoreId(user.getId(), store.getId()).isPresent();
        if (alreadyAssigned) return;

        UserStore userStore = UserStore.builder()
                .userId(user.getId())
                .storeId(store.getId())
                .build();

        userStoreRepository.save(userStore);
    }

    public void removeUserFromStore(UUID userId, UUID storeId, UUID keycloakId) {
        User currentUser = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));

        if (!currentUser.getCompanyId().equals(user.getCompanyId()) ||
                !currentUser.getCompanyId().equals(store.getCompanyId())) {
            throw new IllegalArgumentException("Store and user must belong to the same company as the owner");
        }

        userStoreRepository.deleteByUserIdAndStoreId(userId, storeId);
    }

    public List<UserStore> getStoresForUser(UUID userId) {
        return userStoreRepository.findByUserId(userId);
    }

    public List<UserStore> getUsersForStore(UUID storeId) {
        return userStoreRepository.findByStoreId(storeId);
    }
}