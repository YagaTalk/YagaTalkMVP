package com.yagatalk.utill;

public class InvalidAuthorizationException extends RuntimeException {

    public InvalidAuthorizationException(String message) {
        super(message);
    }
}