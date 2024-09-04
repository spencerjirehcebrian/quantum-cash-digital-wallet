package com.quantumdragon.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.quantumdragon.userservice.enums.KycStatus;

@Entity
@Table(name = "user_profiles", schema = "user_service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    private UUID userId;

    @MapsId
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 255)
    private String addressLine1;

    @Column(length = 255)
    private String addressLine2;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(length = 20)
    private String postalCode;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private KycStatus kycStatus = KycStatus.PENDING;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONEDEFAULT CURRENT_TIMESTAMP")
    private ZonedDateTime updatedAt = ZonedDateTime
            .now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }
}
