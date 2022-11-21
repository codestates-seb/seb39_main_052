package com.seb39.myfridge.domain.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb39.myfridge.domain.auth.enums.AppAuthExceptionCode;
import com.seb39.myfridge.domain.auth.exception.AppAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Login 실패시 호출되는 handler
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if(exception instanceof BadCredentialsException)
            throw new AppAuthenticationException(AppAuthExceptionCode.INVALID_EMAIL_OR_PASSWORD);

        if(exception instanceof AppAuthenticationException)
            throw exception;

        if(exception.getCause() instanceof AppAuthenticationException){
            throw (AppAuthenticationException) exception.getCause();
        }

        throw new AppAuthenticationException(AppAuthExceptionCode.UNDEFINED);
    }
}