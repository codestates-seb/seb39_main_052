package com.seb39.myfridge.domain.auth.handler;

import com.seb39.myfridge.domain.auth.enums.AppAuthExceptionCode;
import com.seb39.myfridge.domain.auth.exception.AppAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        throw new AppAuthenticationException(AppAuthExceptionCode.OAUTH2_AUTH_FAILURE,exception.getMessage());
    }
}
