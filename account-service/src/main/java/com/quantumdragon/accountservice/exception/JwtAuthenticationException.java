package com.quantumdragon.accountservice.exception;

public class JwtAuthenticationException extends RuntimeException {
    public JwtAuthenticationException(String e) {
        super(e);
    }
}
