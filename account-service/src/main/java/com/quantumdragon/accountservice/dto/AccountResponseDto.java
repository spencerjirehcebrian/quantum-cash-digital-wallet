package com.quantumdragon.accountservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.quantumdragon.accountservice.enums.Status;

public record AccountResponseDto(
                UUID id,
                UUID userId,
                String accountType,
                BigDecimal balance,
                String currency,
                Status status) {
}
