package com.asusoftware.SmartMag_Api.shift.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ShiftDto {
    private UUID id;
    private UUID storeId;
    private UUID userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
