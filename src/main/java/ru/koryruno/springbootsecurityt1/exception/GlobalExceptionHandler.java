package ru.koryruno.springbootsecurityt1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> notFoundExceptionHandle(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found" + e.getMessage());
    }

    @ExceptionHandler(AuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> notFoundExceptionHandle(AuthException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed" + e.getMessage());
    }

    @ExceptionHandler(ApplicationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> notFoundExceptionHandle(ApplicationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Validation creation failed" + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> notFoundExceptionHandle(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred" + e.getMessage());
    }

}
