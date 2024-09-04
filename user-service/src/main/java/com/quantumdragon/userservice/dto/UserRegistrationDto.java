package com.quantumdragon.userservice.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegistrationDto(
                @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
                @NotBlank(message = "Password is required") @Size(min = 8, max = 100, message = "Pass must be between 8 and 100 characters") String passwordHash,
                @NotNull(message = "First name is required") String firstName,
                @NotNull(message = "Last name is required") String lastName,
                @NotNull(message = "Date of birth is required") @Past(message = "Date of birth must be in the past") LocalDate dateOfBirth,
                @Pattern(regexp = "\\+?[0-9. ()-]{7,25}", message = "Invalid phone number format") @Size(max = 20, message = "Phone number cannot exceed 20 characters") String phoneNumber,
                @Size(max = 255, message = "Address line 1 cannot exceed 255 characters") String addressLine1,
                @Size(max = 255, message = "Address line 2 cannot exceed 255 characters") String addressLine2,
                @Size(max = 100, message = "City cannot exceed 100 characters") String city,
                @Size(max = 100, message = "State cannot exceed 100 characters") String state,
                @Size(max = 100, message = "Country cannot exceed 100 characters") String country,
                @Size(max = 20, message = "Zip code cannot exceed 20 characters") String postalCode) {

}
