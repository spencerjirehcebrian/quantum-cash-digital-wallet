package com.quantumdragon.accountservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public record AccountDepositRequestDto(@NotNull(message = "Deposit amount is mandatory") BigDecimal depositAmount) {

}
