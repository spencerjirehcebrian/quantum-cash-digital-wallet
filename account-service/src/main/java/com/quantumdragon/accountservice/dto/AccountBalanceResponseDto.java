package com.quantumdragon.accountservice.dto;

import java.math.BigDecimal;

public record AccountBalanceResponseDto(
        BigDecimal balance) {
}
