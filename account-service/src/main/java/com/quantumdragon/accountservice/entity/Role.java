package com.quantumdragon.accountservice.entity;

import java.util.UUID;

import com.quantumdragon.accountservice.enums.RoleName;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue
    private UUID roleId;

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private RoleName roleName;
}