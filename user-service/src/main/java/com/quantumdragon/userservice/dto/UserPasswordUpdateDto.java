package com.quantumdragon.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserPasswordUpdateDto(
        @NotBlank(message = "Password is required") @Size(min = 8, max = 100, message = "Pass must be between 8 and 100 characters") String newPassword) {
}
