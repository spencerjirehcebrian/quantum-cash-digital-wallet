package com.quantumdragon.accountservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;

public record AccountUpdateRequestDto(
        @NotBlank(message = "Account type is mandatory") String accountType,
         BigDecimal balance) {
}
