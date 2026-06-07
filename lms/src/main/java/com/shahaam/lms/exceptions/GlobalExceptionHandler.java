package com.shahaam.lms.exceptions;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

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
        DuplicatePupilException.class
    })
    public ResponseEntity<ErrorResponse> handleDuplicateEntity(
        RuntimeException ex,
        HttpServletRequest request) {
            
            ErrorResponse body = new ErrorResponse(
                409, "Conflict", ex.getMessage(), request.getRequestURI()
            );

        return ResponseEntity.status(409).body(body);
    }

    @ExceptionHandler({
        BookAlreadyBorrowedException.class,
        MemberLimitExceededException.class
    })
    public ResponseEntity<ErrorResponse> handleBusinessRule(
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
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .collect(Collectors.joining(", "));
        return ResponseEntity.status(400).body(new ErrorResponse(400, "Bad Request", message, req.getRequestURI()));
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
