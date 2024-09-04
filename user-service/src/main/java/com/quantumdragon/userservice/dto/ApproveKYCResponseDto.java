package com.quantumdragon.userservice.dto;

public record ApproveKYCResponseDto(
        String userId,
        String stripeCustomerId) {
}
