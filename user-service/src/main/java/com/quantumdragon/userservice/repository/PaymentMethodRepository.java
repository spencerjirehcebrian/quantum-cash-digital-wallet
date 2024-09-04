package com.quantumdragon.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quantumdragon.userservice.entity.PaymentMethod;

import java.util.UUID;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID> {
}
