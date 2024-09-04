package com.quantumdragon.accountservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record AccountWithdrawRequestDto(@NotNull(message = "Withdraw amount is mandatory") BigDecimal withdrawAmount) {

}
