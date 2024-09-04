package com.quantumdragon.accountservice.dto;

import jakarta.validation.constraints.NotBlank;

public record AccountCreateRequestDto(
        String stripeAccountId,
        @NotBlank(message = "Account type is mandatory") String accountType,
        @NotBlank(message = "Currency is mandatory") String currency) {
}
