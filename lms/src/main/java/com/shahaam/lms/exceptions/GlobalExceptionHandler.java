package com.shahaam.lms.exceptions;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import tools.jackson.databind.exc.InvalidFormatException;
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(
        RuntimeException ex,
        HttpServletRequest request) {
            
            ErrorResponse body = new ErrorResponse(
                401, "Unauthorized", ex.getMessage(), request.getRequestURI()
            );

        return ResponseEntity.status(401).body(body);
    }
    
    @ExceptionHandler({BookNotFoundException.class,
        MemberNotFoundException.class,
        WorkerNotFoundException.class,
        NoActiveBorrowRecordFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(
        RuntimeException ex,
        HttpServletRequest request) {
            
            ErrorResponse body = new ErrorResponse(
                404, "Not Found", ex.getMessage(), request.getRequestURI()
            );

        return ResponseEntity.status(404).body(body);
    }
 
    @ExceptionHandler({BookNotAvailableException.class,
        DuplicateISBNException.class,
        DuplicatePupilException.class,
        InvalidBookTypeException.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(
        RuntimeException ex,
        HttpServletRequest request) {
            
            ErrorResponse body = new ErrorResponse(
                409, "Conflict", ex.getMessage(), request.getRequestURI()
            );

        return ResponseEntity.status(409).body(body);
    }

    @ExceptionHandler({
        BookAlreadyBorrowedException.class,
        MemberLimitExceededException.class,
        IllegalArgumentException.class
    })
    public ResponseEntity<ErrorResponse> handleBusinessRule(
        RuntimeException ex,
        HttpServletRequest request) {
            
            ErrorResponse body = new ErrorResponse(
                422, "Unprocessable Content", ex.getMessage(), request.getRequestURI()
            );

        return ResponseEntity.status(422).body(body);
    }

    @ExceptionHandler({InvalidISBNException.class})
    public ResponseEntity<ErrorResponse> handleInvalidISBN(
        RuntimeException ex,
        HttpServletRequest request) {

            ErrorResponse body = new ErrorResponse(
                422, "Unprocessable Content", ex.getMessage(), request.getRequestURI()
            );

            return ResponseEntity.status(422).body(body);
    }

    // Spring's validation exception — unrelated, needs its own handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest req) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> {
                String field = e.getField() != null ? e.getField() : e.getObjectName();
                return field + ": " + e.getDefaultMessage();
            })
            .collect(Collectors.joining(", "));
        return ResponseEntity.status(400).body(
            new ErrorResponse(400, "Bad Request", message, req.getRequestURI())
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest req) {

        String message = "Invalid or malformed request body";

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException ife) {
            if (ife.getTargetType() != null && ife.getTargetType().isEnum()) {
                String fieldName = ife.getPath().isEmpty() ? "unknown"
                    : ife.getPath().get(0).getPropertyName();
                message = "Invalid value '" + ife.getValue() + "' for field '" + fieldName + "'";
            }
        }

        return ResponseEntity.status(400).body(
            new ErrorResponse(400, "Bad Request", message, req.getRequestURI())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        // Log it — don't expose internal details to client
        ex.printStackTrace();

        ErrorResponse body = new ErrorResponse(
            500, "Internal Server Error", "An unexpected error occurred", request.getRequestURI()
        );
        return ResponseEntity.status(500).body(body);
    }
}