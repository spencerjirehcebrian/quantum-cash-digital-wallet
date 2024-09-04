package com.quantumdragon.userservice.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PaymentMethodDto(
                UUID id,
                UUID userId,
                @NotBlank(message = "Method type is required") @Size(max = 50, message = "Method type must be less than 50 characters") String methodType,
                @NotBlank(message = "Provider is required") @Size(max = 50, message = "Provider must be less than 50 characters") String provider,
                @NotBlank(message = "Token ID is required") @Size(max = 100, message = "Token ID must be less than 100 characters") String tokenId,
                @NotBlank(message = "Last 4 digits are required") @Size(min = 4, max = 4, message = "Last 4 digits must be exactly 4 characters") String last4) {
}