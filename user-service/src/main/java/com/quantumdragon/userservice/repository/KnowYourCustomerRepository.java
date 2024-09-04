package com.quantumdragon.userservice.repository;

import com.quantumdragon.userservice.entity.KnowYourCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface KnowYourCustomerRepository extends JpaRepository<KnowYourCustomer, UUID> {
}
