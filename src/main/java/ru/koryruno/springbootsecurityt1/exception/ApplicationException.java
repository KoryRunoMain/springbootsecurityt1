package ru.koryruno.springbootsecurityt1.exception;

public class ApplicationException extends RuntimeException {

    public ApplicationException() {}

    public ApplicationException(String message) {
        super(message);
    }

}
