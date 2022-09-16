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
    public ResponseEntity<AuthResponse> handleAuthenticationException(AuthenticationException e){
        if(e instanceof AppAuthenticationException){
            AuthResponse response = AuthResponse.failure(((AppAuthenticationException) e).getExceptionCode());
            return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
        }
        AuthResponse response = AuthResponse.failure(e.getMessage());
        return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
    }
}
