package com.quantumdragon.userservice.dto;

import jakarta.validation.constraints.Email;

public record UserEmailUpdateDto(@Email(message = "Invalid email format <3") String email) {
}
