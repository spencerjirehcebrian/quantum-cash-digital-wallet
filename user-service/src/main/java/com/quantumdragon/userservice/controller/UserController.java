package com.quantumdragon.userservice.controller;

import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import com.quantumdragon.userservice.dto.*;
import com.quantumdragon.userservice.enums.Status;
import com.quantumdragon.userservice.service.KnowYourCustomerService;
import com.quantumdragon.userservice.service.PaymentMethodService;
import com.quantumdragon.userservice.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private KnowYourCustomerService kycService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    private final Log logger = LogFactory.getLog(UserController.class);

    @PostMapping()
    public StandardResponseDto<UserResponseWithUserProfileDto> register(
            @Valid @RequestBody UserRegistrationDto userDto) {
        UserResponseWithUserProfileDto response = userService.save(userDto);
        return new StandardResponseDto<>(true, "User registered successfully", response);
    }

    @GetMapping("/{userId}")
    public StandardResponseDto<UserResponseWithUserProfileDto> retrieveById(@Valid @PathVariable UUID userId) {
        try {
            UserResponseWithUserProfileDto response = userService.retrieveById(userId);
            return new StandardResponseDto<>(true, "User retrieved successfully", response);
        } catch (Exception e) {
            throw new IllegalArgumentException("User not found");
        }
    }

    @GetMapping("/{userId}/email")
    public StandardResponseDto<String> retrieveEmailById(@Valid @PathVariable UUID userId) {
        try {
            String response = userService.retrieveEmailById(userId);
            return new StandardResponseDto<>(true, "User retrieved successfully", response);
        } catch (Exception e) {
            throw new IllegalArgumentException("User not found");
        }
    }

    @PostMapping("/login")
    public StandardResponseDto<AuthResponseDto> createAuthenticationToken(@Valid @RequestBody UserLoginDto loginDto)
            throws AuthenticationException {
        AuthResponseDto response = userService.authenticate(loginDto);
        return new StandardResponseDto<>(true, "Authentication successful", response);
    }

    @PutMapping("/{userId}")
    public StandardResponseDto<UserResponseDto> updateUser(@Valid @PathVariable UUID userId,
            @Valid @RequestBody UserUpdateDto userDto) {
        UserResponseDto response = userService.update(userId, userDto);
        return new StandardResponseDto<>(true, "User updated successfully", response);
    }

    @PutMapping("/{userId}/update-password")
    public StandardResponseDto<String> updatePassword(@Valid @PathVariable UUID userId,
            @Valid @RequestBody UserPasswordUpdateDto passwordUpdateDto) {
        String response = userService.updatePassword(userId, passwordUpdateDto.newPassword());
        return new StandardResponseDto<>(true, "Password updated successfully", response);
    }

    @PutMapping("/{userId}/update-email")
    public StandardResponseDto<UserResponseDto> updateEmail(@Valid @PathVariable UUID userId,
            @Valid @RequestBody UserEmailUpdateDto emailUpdate, @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        logger.info("Token: " + token);
        logger.info("Email: " + emailUpdate);
        UserResponseDto response = userService.updateEmail(userId, emailUpdate);
        logger.info("Response: " + response);
        userService.logout(token);
        return new StandardResponseDto<>(true, "Email updated successfully. You will need to login again", response);
    }

    @GetMapping("/{userId}/status")
    public StandardResponseDto<Status> checkUserStatus(@Valid @PathVariable UUID userId) {
        Status status = userService.checkUserStatus(userId);
        return new StandardResponseDto<>(true, "User status retrieved successfully", status);
    }

    @DeleteMapping("/{userId}")
    public StandardResponseDto<String> deleteUser(@Valid @PathVariable UUID userId,
            @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        userService.delete(userId);
        userService.logout(token);
        return new StandardResponseDto<>(true, "User deleted successfully", "User deleted successfully");
    }

    @GetMapping("/logout")
    public StandardResponseDto<String> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = extractToken(authHeader);
            userService.logout(token);
            return new StandardResponseDto<>(true, "Logout successful", "Logout successful");
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal token or token not found");
        }
    }

    @PostMapping("/{userId}/kyc")
    public StandardResponseDto<String> intiateKyc(@Valid @PathVariable UUID userId) {
        String response = kycService.createEmptyKYCForUser(userId);
        return new StandardResponseDto<String>(true, "KYC initiated successfully", response);
    }

    @PostMapping("/{userId}/kyc/submit")
    public StandardResponseDto<KnowYourCustomerDto> submitKyc(@Valid @PathVariable UUID userId,
            @Valid @RequestBody KnowYourCustomerDto dto) {
        KnowYourCustomerDto response = kycService.updateKYC(userId, dto);
        return new StandardResponseDto<KnowYourCustomerDto>(true, "KYC details submitted successfully", response);
    }

    @PostMapping("/{userId}/payment-method")
    public StandardResponseDto<PaymentMethodDto> addPaymentMethod(@Valid @PathVariable UUID userId,
            @Valid @RequestBody PaymentMethodDto dto) {
        PaymentMethodDto response = paymentMethodService.addPaymentMethod(userId, dto);
        return new StandardResponseDto<PaymentMethodDto>(true, "Payment method added successfully", response);
    }

    @GetMapping("/{userId}/payment-method/{paymentMethodId}")
    public StandardResponseDto<PaymentMethodDto> getPaymentMethod(@Valid @PathVariable UUID userId,
            @Valid @PathVariable UUID paymentMethodId) {
        PaymentMethodDto response = paymentMethodService.getPaymentMethod(userId, paymentMethodId);
        return new StandardResponseDto<PaymentMethodDto>(true, "Payment method retreived successfully", response);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('SUPERADMIN')")
    @GetMapping("/{userId}/kyc")
    public StandardResponseDto<KnowYourCustomerDto> retrieveKyc(@Valid @PathVariable UUID userId) {
        KnowYourCustomerDto response = kycService.retreiveKYC(userId);
        return new StandardResponseDto<KnowYourCustomerDto>(true, "KYC retrieved successfully",
                response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    @GetMapping("/all")
    public StandardResponseDto<List<UserResponseWithUserProfileDto>> retrieveAll() {
        List<UserResponseWithUserProfileDto> response = userService.retrieveAll();
        return new StandardResponseDto<>(true, "All users retrieved successfully", response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    @GetMapping("/kyc/all")
    public StandardResponseDto<List<KnowYourCustomerDto>> retrieveKycAll() {
        List<KnowYourCustomerDto> response = kycService.retreiveKYCAll();
        return new StandardResponseDto<>(true, "All KYC retrieved successfully", response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    @PutMapping("/{userId}/kyc/approve")
    public StandardResponseDto<ApproveKYCResponseDto> approveKyc(@Valid @PathVariable UUID userId,
            @RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        ApproveKYCResponseDto response = userService.approveKYC(userId, token);
        return new StandardResponseDto<>(true, "KYC Approved",
                response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPERADMIN')")
    @PutMapping("/{userId}/kyc/reject")
    public StandardResponseDto<String> rejectKyc(@Valid @PathVariable UUID userId) {
        userService.rejectKYC(userId);
        return new StandardResponseDto<>(true, "KYC Rejected", "KYC Rejected for: " + userId);
    }

    @PreAuthorize("hasRole('SUPERADMIN')")
    @PostMapping("/register-admin")
    public StandardResponseDto<UserResponseWithUserProfileDto> registerAdmin(
            @Valid @RequestBody UserRegistrationDto userDto) {
        UserResponseWithUserProfileDto response = userService.saveAdmin(userDto);
        return new StandardResponseDto<>(true, "User registered successfully", response);
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return authHeader;
    }

}
