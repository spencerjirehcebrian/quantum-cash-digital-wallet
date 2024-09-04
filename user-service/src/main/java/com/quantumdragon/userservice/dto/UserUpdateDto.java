package com.quantumdragon.userservice.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateDto(
        String stripeCustomerId,
        String firstName,
        String lastName,
        @Past(message = "Date of birth must be in the past") LocalDate dateOfBirth,

        @Pattern(regexp = "\\+?[0-9. ()-]{7,25}", message = "Invalid phone number format") @Size(max = 20, message = "Phone number cannot exceed 20 characters") String phoneNumber) {
}
