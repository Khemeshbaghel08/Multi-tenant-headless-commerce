package com.example.demo.handler;

import com.example.demo.entity.ApiError;
import com.example.demo.exceptions.CartExc;
import com.example.demo.exceptions.ForbiddenExc;
import com.example.demo.exceptions.InventoryExc;
import com.example.demo.exceptions.UnauthorizeExc;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizeExc.class)
    public ResponseEntity<ApiError> handleUnauthorized(
            UnauthorizeExc ex,
            HttpServletRequest request
    ) {
        return build(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage(),
                request
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ForbiddenExc.class)
    public ResponseEntity<ApiError> handleForbidden(
            ForbiddenExc ex,
            HttpServletRequest request
    ) {
        return build(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InventoryExc.class)
    public ResponseEntity<ApiError> handleInventory(
            InventoryExc ex,
            HttpServletRequest request
    ) {
        return build(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CartExc.class)
    public ResponseEntity<ApiError> handleCart(
            CartExc ex,
            HttpServletRequest request
    ) {
        return build(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request
        );
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(
            Exception ex,
            HttpServletRequest request
    ) {
        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong",
                request
        );
    }

    private ResponseEntity<ApiError> build(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {
        ApiError error = new ApiError(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                Instant.now()
        );
        return ResponseEntity.status(status).body(error);
    }
}
