package com.asusoftware.SmartMag_Api.attendance.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckInRequestDto {
    @NotNull
    private UUID storeId;
}