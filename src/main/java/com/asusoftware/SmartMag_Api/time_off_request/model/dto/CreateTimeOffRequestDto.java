package com.asusoftware.SmartMag_Api.time_off_request.model.dto;

import com.asusoftware.SmartMag_Api.time_off_request.model.TimeOffRequestType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateTimeOffRequestDto {

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private TimeOffRequestType type;

    private String reason;
}
