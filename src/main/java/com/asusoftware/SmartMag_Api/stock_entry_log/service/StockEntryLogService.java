package com.asusoftware.SmartMag_Api.stock_entry_log.service;

import com.asusoftware.SmartMag_Api.exception.ResourceNotFoundException;
import com.asusoftware.SmartMag_Api.product.model.Product;
import com.asusoftware.SmartMag_Api.product.repository.ProductRepository;
import com.asusoftware.SmartMag_Api.stock_entry_log.model.StockEntryLog;
import com.asusoftware.SmartMag_Api.stock_entry_log.model.dto.CreateStockEntryLogDto;
import com.asusoftware.SmartMag_Api.stock_entry_log.model.dto.StockEntryLogDto;
import com.asusoftware.SmartMag_Api.stock_entry_log.repository.StockEntryLogRepository;
import com.asusoftware.SmartMag_Api.store.model.Store;
import com.asusoftware.SmartMag_Api.store.repository.StoreRepository;
import com.asusoftware.SmartMag_Api.user.model.User;
import com.asusoftware.SmartMag_Api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockEntryLogService {

    private final StockEntryLogRepository stockEntryLogRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final ModelMapper mapper;

    public StockEntryLogDto create(CreateStockEntryLogDto dto, UUID keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Store store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));

        if (!product.getCompanyId().equals(user.getCompanyId()) ||
                !store.getCompanyId().equals(user.getCompanyId())) {
            throw new IllegalArgumentException("Access denied: entities do not belong to your company");
        }

        StockEntryLog log = StockEntryLog.builder()
                .productId(product.getId())
                .storeId(store.getId())
                .userId(user.getId())
                .quantity(dto.getQuantity())
                .type(dto.getType())
                .reason(dto.getReason())
                .date(LocalDateTime.now())
                .build();

        log = stockEntryLogRepository.save(log);
        return mapper.map(log, StockEntryLogDto.class);
    }

    public List<StockEntryLogDto> getFilteredLogs(UUID productId, UUID storeId) {
        List<StockEntryLog> logs;

        if (productId != null && storeId != null) {
            logs = stockEntryLogRepository.findByProductIdAndStoreId(productId, storeId);
        } else if (productId != null) {
            logs = stockEntryLogRepository.findByProductId(productId);
        } else if (storeId != null) {
            logs = stockEntryLogRepository.findByStoreId(storeId);
        } else {
            logs = stockEntryLogRepository.findAll();
        }

        return logs.stream()
                .map(log -> mapper.map(log, StockEntryLogDto.class))
                .collect(Collectors.toList());
    }
}
