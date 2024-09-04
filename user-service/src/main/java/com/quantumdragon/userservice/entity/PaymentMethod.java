package com.quantumdragon.userservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "payment_methods", schema = "user_service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "method_type", nullable = false)
    private String methodType;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "token_id", nullable = false)
    private String tokenId;

    @Column(name = "last4", nullable = false)
    private String last4;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

}
