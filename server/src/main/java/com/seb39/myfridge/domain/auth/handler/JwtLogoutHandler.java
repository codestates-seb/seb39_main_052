package com.seb39.myfridge.domain.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb39.myfridge.domain.auth.domain.AuthenticationToken;
import com.seb39.myfridge.domain.auth.service.AuthenticationTokenService;
import com.seb39.myfridge.domain.auth.util.AuthenticationTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final AuthenticationTokenService tokenService;
    private final ObjectMapper om;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        AuthenticationToken token = AuthenticationTokenUtils.getTokenFromRequest(request);
        tokenService.removeToken(token);
        AuthenticationTokenUtils.removeTokenInResponse(response);
    }
}
