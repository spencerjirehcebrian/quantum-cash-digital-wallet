package com.quantumdragon.userservice.dto;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import com.quantumdragon.userservice.enums.Status;

public record UserResponseWithUserProfileDto(
                UUID id,
                String stripeCustomerId,
                String firstName,
                String lastName,
                String email,
                String phoneNumber,
                Status status,
                ZonedDateTime createdAt,
                ZonedDateTime updatedAt,
                Set<String> roles,
                UserProfileResponseDto userProfile

) {
}
