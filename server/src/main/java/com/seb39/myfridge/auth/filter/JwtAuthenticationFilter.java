package com.seb39.myfridge.auth.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb39.myfridge.auth.PrincipalDetails;
import com.seb39.myfridge.auth.dto.LoginRequest;
import com.seb39.myfridge.auth.enums.AppAuthExceptionCode;
import com.seb39.myfridge.auth.exception.AppAuthenticationException;
import com.seb39.myfridge.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        LoginRequest loginRequest = getLoginRequest(request);

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManager.authenticate(token);
    }

    private LoginRequest getLoginRequest(HttpServletRequest request) {
        LoginRequest loginRequest = null;
        try {
            loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        } catch (IOException e) {
            throw new AppAuthenticationException(AppAuthExceptionCode.DATA_DESERIALIZE_ERROR);
        }
        return loginRequest;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principal = (PrincipalDetails) authResult.getPrincipal();

        String accessToken = jwtService.createAccessToken(principal.getMemberId(), principal.getUsername());
        String headerValue = jwtService.accessTokenToAuthorizationHeader(accessToken);
        response.addHeader(HttpHeaders.AUTHORIZATION, headerValue);

        String refreshToken = jwtService.createRefreshToken(accessToken);
        response.addCookie(jwtService.refreshTokenToCookie(refreshToken));
    }
}