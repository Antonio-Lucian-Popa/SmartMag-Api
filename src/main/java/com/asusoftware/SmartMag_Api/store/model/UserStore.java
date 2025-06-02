package com.asusoftware.SmartMag_Api.store.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user_store")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "store_id", nullable = false)
    private UUID storeId;
}
