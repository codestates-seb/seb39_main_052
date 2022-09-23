package com.seb39.myfridge.advice;

import com.seb39.myfridge.auth.dto.AuthResponse;
import com.seb39.myfridge.auth.enums.AppAuthExceptionCode;
import com.seb39.myfridge.auth.exception.AppAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AuthResponse handleAuthenticationException(AuthenticationException e){
        if(e instanceof AppAuthenticationException){
             return AuthResponse.failure(((AppAuthenticationException) e).getExceptionCode());
        }
        return AuthResponse.failure(e.getMessage());
    }
}
