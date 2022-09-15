package com.seb39.myfridge.auth.exception;

import org.springframework.security.core.AuthenticationException;

public class AppAuthenticationException extends AuthenticationException {
    public AppAuthenticationException(String msg) {
        super(msg);
    }
}
