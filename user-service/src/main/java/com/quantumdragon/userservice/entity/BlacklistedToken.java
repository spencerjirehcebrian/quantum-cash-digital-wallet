package com.quantumdragon.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "blacklisted_tokens", schema = "user_service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistedToken {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Token cannot be blank")
    @Size(min = 10, max = 500, message = "Token must be between 10 and 500 characters")
    private String token;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    @NotNull(message = "Blacklisted timestamp is required")
    @PastOrPresent(message = "Blacklisted timestamp must be in the past or present")
    private ZonedDateTime blacklistedAt = ZonedDateTime.now();

    @PrePersist
    protected void onCreate() {
        blacklistedAt = ZonedDateTime.now();
    }
}
