package com.seb39.myfridge.auth.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb39.myfridge.auth.dto.AuthResponse;
import com.seb39.myfridge.auth.exception.AppAuthenticationException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.IOException;


@Slf4j
public class JwtExceptionHandlingFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (AppAuthenticationException e) {
            log.info("AppAuthenticationException", e);
            AuthResponse responseBody = AuthResponse.failure(e.getExceptionCode());
            objectMapper.writeValue(response.getWriter(),responseBody);
        }
    }
}
