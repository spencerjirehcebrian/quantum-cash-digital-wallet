package com.quantumdragon.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "know_your_customer", schema = "user_service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowYourCustomer {

    @Id
    private UUID userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String idType;
    private String idNumber;
    private ZonedDateTime idExpiryDate;
    private String addressProofType;

    @Column(columnDefinition = "BYTEA")
    private byte[] addressProofDocument;

    @Column(columnDefinition = "BYTEA")
    private byte[] idProofDocument;
}
