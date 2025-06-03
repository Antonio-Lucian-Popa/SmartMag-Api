package com.asusoftware.SmartMag_Api.shift_swap_history.model;

import com.asusoftware.SmartMag_Api.shift_swap_request.model.ShiftSwapStatus;
import com.asusoftware.SmartMag_Api.shift_swap_request.model.ShiftType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
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

    @Column(name = "old_user_id", nullable = false)
    private UUID oldUserId;

    @Column(name = "new_user_id", nullable = false)
    private UUID newUserId;

    @Column(name = "store_id", nullable = false)
    private UUID storeId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "shift_type", nullable = false)
    private ShiftType shiftType;

    @Column(name = "approved_by")
    private UUID approvedBy;

    @CreationTimestamp
    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;
}

