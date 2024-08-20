package ru.koryruno.springbootsecurityt1.exception;

public class UnauthorizedException extends Throwable {

    public UnauthorizedException() {}

    public UnauthorizedException(String message) {
        super(message);
    }

}
