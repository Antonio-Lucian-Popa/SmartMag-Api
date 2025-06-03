package com.asusoftware.SmartMag_Api.time_off_request.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "time_off_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeOffRequest {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeOffRequestType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeOffRequestStatus status = TimeOffRequestStatus.PENDING;

    private String reason;
}
