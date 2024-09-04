package com.quantumdragon.accountservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AccountTransferRequestDto(@NotNull(message = "Transfer amount is mandatory") BigDecimal transferAmount,
        @NotNull(message = "To account ID is mandatory") UUID toAccountId) {

}
