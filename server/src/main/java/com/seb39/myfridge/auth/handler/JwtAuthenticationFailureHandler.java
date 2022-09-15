package com.seb39.myfridge.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb39.myfridge.auth.dto.AuthResponse;
import com.seb39.myfridge.auth.enums.AppAuthExceptionCode;
import com.seb39.myfridge.auth.exception.AppAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
//        log.info("Authentication failure.",exception);
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        AuthResponse authResponse = AuthResponse.failure("Invalid username or password");
//        objectMapper.writeValue(response.getWriter(),authResponse);
        throw new AppAuthenticationException(AppAuthExceptionCode.INVALID_EMAIL_OR_PASSWORD);
    }
}