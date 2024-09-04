package com.quantumdragon.userservice.dto;

import java.util.UUID;

import jakarta.validation.constraints.Pattern;

public record UserRetrieveByIdDto(
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format") UUID userId) {
}
