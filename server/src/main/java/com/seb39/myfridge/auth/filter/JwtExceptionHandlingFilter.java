package com.seb39.myfridge.auth.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb39.myfridge.auth.dto.AuthResponse;
import com.seb39.myfridge.auth.exception.AppAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class JwtExceptionHandlingFilter extends BasicAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtExceptionHandlingFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (AppAuthenticationException e) {
            log.info("catch AppAuthException {}", e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            AuthResponse responseBody = AuthResponse.failure(e.getExceptionCode());
            objectMapper.writeValue(response.getWriter(),responseBody);
        }catch(Exception e){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            AuthResponse responseBody = AuthResponse.failure(e.getMessage());
            objectMapper.writeValue(response.getWriter(),responseBody);
        }
    }
}
