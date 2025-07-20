package com.zeogrid.zeover.exception;

public class UserAlreadyRegisterException extends RuntimeException {
    public UserAlreadyRegisterException(String message) {
        super(message);
    }
}
