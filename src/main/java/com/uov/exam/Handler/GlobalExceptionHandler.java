package com.uov.exam.Handler;

import com.uov.exam.Exception.UserNotFoundException;
import com.uov.exam.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle UserNotFoundException specifically
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        // Log error details
        logger.error("User not found: {}", ex.getMessage(), ex);

        // Create structured error response
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                "USER_NOT_FOUND",             // Custom error code for user not found
                System.currentTimeMillis(),   // Timestamp of error occurrence
                "The user you are looking for does not exist in the system."  // Error description
        );

        // Return error response with HTTP 404 (Not Found)
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Handle generic RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        // Log runtime error details
        logger.error("Runtime error occurred: {}", ex.getMessage(), ex);

        // Create structured error response
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                "SERVER_ERROR",               // Custom error code for internal server error
                System.currentTimeMillis(),
                "An unexpected server error occurred."
        );

        // Return error response with HTTP 500 (Internal Server Error)
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle any other generic exceptions (fallback for unexpected errors)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        // Log general error details
        logger.error("Unexpected error: {}", ex.getMessage(), ex);

        // Create structured error response
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                "GENERAL_ERROR",              // Custom error code for general errors
                System.currentTimeMillis(),
                "An unexpected error occurred. Please try again later."
        );

        // Return error response with HTTP 400 (Bad Request) for unexpected exceptions
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
