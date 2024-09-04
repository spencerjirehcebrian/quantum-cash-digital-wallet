package com.quantumdragon.userservice.exception;

public class JwtAuthenticationException extends RuntimeException {
    public JwtAuthenticationException(String e) {
        super(e);
    }
}
