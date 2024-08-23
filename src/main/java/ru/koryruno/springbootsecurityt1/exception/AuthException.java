package ru.koryruno.springbootsecurityt1.exception;

public class AuthException extends RuntimeException {

    public AuthException() {}

    public AuthException(String message) {
        super(message);
    }

}
