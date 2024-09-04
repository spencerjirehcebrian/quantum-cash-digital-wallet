package com.quantumdragon.userservice.exception;

import com.quantumdragon.userservice.dto.StandardResponseDto;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.common.errors.ResourceNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Log logger = LogFactory.getLog(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardResponseDto<String>> handleResourceNotFound(ResourceNotFoundException ex,
            WebRequest request) {
        return createErrorResponse(HttpStatus.NOT_FOUND, "Resource not found", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardResponseDto<String>> illegalArgumentException(IllegalArgumentException ex,
            WebRequest request) {
        return createErrorResponse(HttpStatus.NOT_FOUND, "Illegal argument exception", ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<StandardResponseDto<String>> handleInvalidCredentials(InvalidCredentialsException ex,
            WebRequest request) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid credentials", ex.getMessage());
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<StandardResponseDto<String>> handleInvalidInput(InvalidInputException ex,
            WebRequest request) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, "Invalid input", ex.getMessage());
    }

    @ExceptionHandler({ UserAlreadyExistsException.class, EmailAlreadyExistsException.class })
    public ResponseEntity<StandardResponseDto<String>> handleAlreadyExistsExceptions(Exception ex, WebRequest request) {
        String message = ex instanceof UserAlreadyExistsException ? "User already exists" : "Email already exists";
        return createErrorResponse(HttpStatus.CONFLICT, message, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardResponseDto<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        StandardResponseDto<Map<String, String>> responseDto = new StandardResponseDto<>(
                false,
                "Validation failed",
                errors);

        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<StandardResponseDto<String>> handleJwtAuthenticationException(JwtAuthenticationException ex) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<StandardResponseDto<String>> handleAuthorizationDeniedException(
            AuthorizationDeniedException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.FORBIDDEN, "Access Denied", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponseDto<String>> handleGenericException(Exception ex, WebRequest request) {
        logger.error("Unhandled exception occurred", ex);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage());
    }

    public ResponseEntity<StandardResponseDto<String>> createErrorResponse(HttpStatus status, String message,
            String detail) {
        StandardResponseDto<String> responseDto = new StandardResponseDto<>(
                false,
                message,
                detail);

        logger.error(message + ": " + detail);
        return new ResponseEntity<>(responseDto, status);
    }
}
