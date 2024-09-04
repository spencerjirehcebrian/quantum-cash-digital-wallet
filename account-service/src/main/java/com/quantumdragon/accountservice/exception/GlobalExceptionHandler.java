package com.quantumdragon.accountservice.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.quantumdragon.accountservice.dto.StandardResponseDto;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Log logger = LogFactory.getLog(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidInputException.class)
    public final ResponseEntity<StandardResponseDto<String>> handleInvalidInputException(
            InvalidInputException ex, WebRequest request) {
        StandardResponseDto<String> errorResponse = new StandardResponseDto<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<StandardResponseDto<String>> handleNoResourceFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        StandardResponseDto<String> errorResponse = new StandardResponseDto<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidAccountTypeException.class)
    public final ResponseEntity<StandardResponseDto<String>> handleInvalidAccountTypeException(
            InvalidAccountTypeException ex, WebRequest request) {
        StandardResponseDto<String> errorResponse = new StandardResponseDto<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenValidationException.class)
    public ResponseEntity<StandardResponseDto<String>> handleTokenValidationException(
            TokenValidationException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.UNAUTHORIZED, "Token Validation Error", ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<StandardResponseDto<String>> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        return createErrorResponse(HttpStatus.FORBIDDEN, "Access Denied", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponseDto<String>> handleGenericException(Exception ex, WebRequest request) {
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
