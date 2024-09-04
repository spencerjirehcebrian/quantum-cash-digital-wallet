package com.quantumdragon.accountservice.dto;

import com.quantumdragon.accountservice.enums.Status;

public record AccountStatusUpdateRequestDto(
        Status status) {
}
