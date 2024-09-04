package com.quantumdragon.accountservice.controller;

import com.quantumdragon.accountservice.dto.*;
import com.quantumdragon.accountservice.service.AccountService;

import jakarta.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    private final Log logger = LogFactory.getLog(AccountService.class);

    private UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            try {
                return UUID.fromString(authentication.getName());
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid UUID string: " + authentication.getName());
                return null;
            }
        }
        return null;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
    @PostMapping
    public ResponseEntity<StandardResponseDto<AccountResponseDto>> createAccount(
            @Valid @RequestBody AccountCreateRequestDto request) {
        UUID currentUserId = getCurrentUserId();
        AccountResponseDto response = accountService.createAccount(request, currentUserId);
        return ResponseEntity.ok(new StandardResponseDto<>(true, "Account created successfully", response));
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/{accountId}")
    public ResponseEntity<StandardResponseDto<AccountResponseDto>> getAccount(@PathVariable UUID accountId) {
        AccountResponseDto response = accountService.getAccount(accountId);
        return ResponseEntity.ok(new StandardResponseDto<>(true, "Account retrieved successfully", response));
    }

    @PreAuthorize("hasUserId(#userId) or hasRole('ROLE_ADMIN')")
    @GetMapping("/users/{userId}")
    public ResponseEntity<StandardResponseDto<List<AccountResponseDto>>> getUserAccounts(@PathVariable UUID userId) {
        UUID currentUserId = getCurrentUserId();
        logger.info("User-specific endpoint for userId: " + userId + " accessed by user: " + currentUserId);
        List<AccountResponseDto> response = accountService.getUserAccounts(userId);
        return ResponseEntity.ok(new StandardResponseDto<>(true, "Accounts retrieved successfully", response));
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
    @PutMapping("/{accountId}/update")
    public ResponseEntity<StandardResponseDto<AccountResponseDto>> updateAccount(@PathVariable UUID accountId,
            @Valid @RequestBody AccountUpdateRequestDto request) {
        AccountResponseDto response = accountService.updateAccount(accountId, request);
        return ResponseEntity.ok(new StandardResponseDto<>(true, "Account updated successfully", response));
    }

    @PutMapping("/{accountId}/deposit")
    public ResponseEntity<StandardResponseDto<AccountResponseDto>> depositToAccount(
            @PathVariable UUID accountId,
            @Valid @RequestBody AccountDepositRequestDto request) {
        AccountResponseDto response = accountService.depositToAccount(accountId, request);
        return ResponseEntity.ok(new StandardResponseDto<>(true, "Amount deposited successfully", response));
    }

    @PutMapping("/{accountId}/withdraw")
    public ResponseEntity<StandardResponseDto<AccountResponseDto>> withdrawFromAccount(
            @PathVariable UUID accountId,
            @Valid @RequestBody AccountWithdrawRequestDto request) {
        AccountResponseDto response = accountService.withdrawFromAccount(accountId, request);
        return ResponseEntity.ok(new StandardResponseDto<>(true, "Amount withdrawn successfully", response));
    }

    @PutMapping("/{accountId}/transfer")
    public ResponseEntity<StandardResponseDto<AccountTransferResponseDto>> transferBetweenAccounts(
            @PathVariable UUID accountId,
            @Valid @RequestBody AccountTransferRequestDto request) {
        AccountTransferResponseDto response = accountService.transferBetweenAccounts(accountId, request);
        return ResponseEntity.ok(new StandardResponseDto<>(true, "Amount withdrawn successfully", response));
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
    @PutMapping("/{accountId}")
    public ResponseEntity<StandardResponseDto<AccountResponseDto>> depositFunds(@PathVariable UUID accountId,
            @Valid @RequestBody AccountUpdateRequestDto request) {
        AccountResponseDto response = accountService.updateAccount(accountId, request);
        return ResponseEntity.ok(new StandardResponseDto<>(true, "Account updated successfully", response));
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
    @DeleteMapping("/{accountId}")
    public ResponseEntity<StandardResponseDto<Void>> deleteAccount(@PathVariable UUID accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok(new StandardResponseDto<>(true, "Account marked as inactive", null));
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
    @GetMapping("/{accountId}/balance")
    public ResponseEntity<StandardResponseDto<AccountBalanceResponseDto>> getAccountBalance(
            @PathVariable UUID accountId) {
        AccountBalanceResponseDto response = accountService.getAccountBalance(accountId);
        return ResponseEntity.ok(new StandardResponseDto<>(true, "Account balance retrieved successfully", response));
    }

}
