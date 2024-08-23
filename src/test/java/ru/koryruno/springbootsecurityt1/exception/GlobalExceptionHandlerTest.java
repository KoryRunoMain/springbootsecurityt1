package ru.koryruno.springbootsecurityt1.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GlobalExceptionHandlerTest {

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void notFoundExceptionHandle_ReturnsNotFound() {
        NotFoundException exception = new NotFoundException("User not found");
        ResponseEntity<String> response = globalExceptionHandler.notFoundExceptionHandle(exception);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("User not found"));
    }

    @Test
    void authenticationExceptionHandle_ReturnsForbidden() {
        AuthException exception = new AuthException("Auth failed");
        ResponseEntity<String> response = globalExceptionHandler.notFoundExceptionHandle(exception);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertTrue(response.getBody().contains("Auth failed"));
    }

    @Test
    void applicationExceptionHandle_ReturnsConflict() {
        ApplicationException exception = new ApplicationException("Validation failed");
        ResponseEntity<String> response = globalExceptionHandler.notFoundExceptionHandle(exception);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody().contains("Validation failed"));
    }

    @Test
    void genericExceptionHandle_ReturnsInternalServerError() {
        Exception exception = new Exception("Unexpected error");
        ResponseEntity<String> response = globalExceptionHandler.notFoundExceptionHandle(exception);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Unexpected error"));
    }

}