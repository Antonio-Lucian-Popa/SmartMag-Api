package com.asusoftware.SmartMag_Api.shift_swap_history.model;

import com.asusoftware.SmartMag_Api.shift_swap_request.model.ShiftSwapStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shift_swap_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftSwapHistory {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID shiftSwapRequestId;
    private UUID userId;
    private ShiftSwapStatus oldStatus;
    private ShiftSwapStatus newStatus;
    private LocalDateTime timestamp;
}

