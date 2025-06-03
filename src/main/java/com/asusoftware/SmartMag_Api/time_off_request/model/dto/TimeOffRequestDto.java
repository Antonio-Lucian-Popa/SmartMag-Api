package com.asusoftware.SmartMag_Api.time_off_request.model.dto;

import com.asusoftware.SmartMag_Api.time_off_request.model.TimeOffRequestStatus;
import com.asusoftware.SmartMag_Api.time_off_request.model.TimeOffRequestType;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class TimeOffRequestDto {
    private UUID id;
    private UUID userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private TimeOffRequestType type;
    private TimeOffRequestStatus status;
    private String reason;
}
