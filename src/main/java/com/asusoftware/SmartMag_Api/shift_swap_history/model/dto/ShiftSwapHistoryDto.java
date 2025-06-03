package com.asusoftware.SmartMag_Api.shift_swap_history.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ShiftSwapHistoryDto {
    private UUID id;
    private UUID oldUserId;
    private UUID newUserId;
    private UUID storeId;
    private LocalDate date;
    private String shiftType;
    private UUID approvedBy;
    private LocalDateTime timestamp;
}

