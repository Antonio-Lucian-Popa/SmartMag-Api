package com.asusoftware.SmartMag_Api.shift_swap_request.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "shift_swap_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftSwapRequest {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "from_user_id", nullable = false)
    private UUID fromUserId;

    @Column(name = "to_user_id", nullable = false)
    private UUID toUserId;

    @Column(name = "store_id", nullable = false)
    private UUID storeId;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "shift_type", nullable = false)
    private ShiftType shiftType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShiftSwapStatus status = ShiftSwapStatus.PENDING;
}

