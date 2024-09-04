package com.quantumdragon.userservice.controller;

import java.util.Set;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.quantumdragon.userservice.entity.TokenValidationRequest;
import com.quantumdragon.userservice.entity.TokenValidationResponse;
import com.quantumdragon.userservice.security.JwtTokenUtil;

import com.quantumdragon.userservice.entity.Role;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtTokenUtil tokenService;

    private final Log logger = LogFactory.getLog(AuthController.class);

    @PostMapping("/validate-token")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestBody TokenValidationRequest request) {
        logger.info("Validating token: " + request.getToken());
        String token = request.getToken();
        boolean isValid = tokenService.validateToken(request.getToken());
        if (isValid) {
            logger.info("Token validated: " + request.getToken());
            Set<Role> roles = tokenService.getRolesFromToken(request.getToken());
            UUID userId = tokenService.getUserIdFromToken(token);
            return ResponseEntity.ok(new TokenValidationResponse(true, roles, userId));
        } else {
            logger.warn("Invalid token: " + request.getToken());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenValidationResponse(false, null, null));
        }
    }

}
