package com.quantumdragon.userservice.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public record AuthResponseDto(
                UUID userId,
                String token,
                LocalDateTime expiresAt,
                List<String> roleNames) {
}
