package com.toostew.noteShare.exception.pojo.service;

public class CustomUserDetailsServiceException extends RuntimeException {
    public CustomUserDetailsServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
