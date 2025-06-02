package com.asusoftware.SmartMag_Api.attendance.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AttendanceDto {
    private UUID id;
    private UUID shiftId;
    private UUID userId;
    private LocalDateTime clockInTime;
    private LocalDateTime clockOutTime;
}
