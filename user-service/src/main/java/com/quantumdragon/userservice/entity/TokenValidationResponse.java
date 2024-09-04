package com.quantumdragon.userservice.entity;

import java.util.Set;
import java.util.UUID;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenValidationResponse {
    private boolean valid;
    private Set<Role> roles;
    private UUID userId;
}
