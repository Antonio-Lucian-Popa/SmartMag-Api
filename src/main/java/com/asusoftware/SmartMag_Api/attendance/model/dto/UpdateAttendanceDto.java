package com.asusoftware.SmartMag_Api.attendance.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateAttendanceDto {

    @NotNull
    private LocalDateTime clockOutTime;
}
