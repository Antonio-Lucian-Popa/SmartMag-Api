package com.asusoftware.SmartMag_Api.shift.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateShiftDto {

    @NotNull
    private UUID storeId;

    @NotNull
    private UUID userId;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;
}
