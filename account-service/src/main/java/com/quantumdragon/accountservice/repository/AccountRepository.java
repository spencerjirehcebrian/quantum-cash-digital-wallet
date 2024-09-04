package com.quantumdragon.accountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quantumdragon.accountservice.entity.Account;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    List<Account> findByUserId(UUID userId);
}