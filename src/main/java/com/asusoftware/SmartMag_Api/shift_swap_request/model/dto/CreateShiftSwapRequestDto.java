package com.asusoftware.SmartMag_Api.shift_swap_request.model.dto;

import com.asusoftware.SmartMag_Api.shift_swap_request.model.ShiftSwapStatus;
import com.asusoftware.SmartMag_Api.shift_swap_request.model.ShiftType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateShiftSwapRequestDto {
    private UUID fromUserId;
    private UUID toUserId;
    private UUID storeId;
    private LocalDate date;
    private ShiftType shiftType;
    private ShiftSwapStatus status = ShiftSwapStatus.PENDING;
}

