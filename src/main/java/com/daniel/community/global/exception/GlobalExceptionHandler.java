package com.daniel.community.global.exception;

import com.daniel.community.global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(
            IllegalArgumentException exception
    ) {
        String message = exception.getMessage();
        HttpStatus status = getStatus(message);

        return ResponseEntity
                .status(status)
                .body(ApiResponse.error(message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("invalid_request"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse> handleAuthenticationException() {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("unauthorized"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException() {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("forbidden"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("internal_server_error"));
    }

    private HttpStatus getStatus(String message) {
        if ("unauthorized".equals(message) || "login_failed".equals(message)) {
            return HttpStatus.UNAUTHORIZED;
        }

        if ("forbidden".equals(message)) {
            return HttpStatus.FORBIDDEN;
        }

        if ("post_not_found".equals(message)
                || "comment_not_found".equals(message)
                || "image_not_found".equals(message)) {
            return HttpStatus.NOT_FOUND;
        }

        if ("email_duplicated".equals(message)
                || "nickname_duplicated".equals(message)) {
            return HttpStatus.CONFLICT;
        }

        if ("internal_server_error".equals(message)) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.BAD_REQUEST;
    }
}