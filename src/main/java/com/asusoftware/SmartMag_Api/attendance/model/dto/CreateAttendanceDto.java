package com.asusoftware.SmartMag_Api.attendance.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateAttendanceDto {

    @NotNull
    private UUID shiftId;

    @NotNull
    private UUID userId;
}
