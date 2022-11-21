package com.seb39.myfridge.domain.auth.handler;

import com.seb39.myfridge.domain.auth.PrincipalDetails;
import com.seb39.myfridge.domain.auth.domain.AuthenticationToken;
import com.seb39.myfridge.domain.auth.service.AuthenticationTokenService;
import com.seb39.myfridge.domain.auth.util.AuthenticationTokenUtils;
import com.seb39.myfridge.domain.auth.util.AppAuthNames;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthenticationTokenService authenticationTokenService;

    @Value("${app.auth.oauth.front-redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Long memberId = principal.getMemberId();
        AuthenticationToken token = authenticationTokenService.issueAuthenticationToken(memberId);
        AuthenticationTokenUtils.addTokenInResponse(response, token);
        getRedirectStrategy().sendRedirect(request,response, createRedirectUri(token.getAccess(),memberId));
    }

    private String createRedirectUri(String accessToken, Long memberId){
        return UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam(AppAuthNames.ACCESS_TOKEN,accessToken)
                .queryParam(AppAuthNames.MEMBER_ID,memberId)
                .build()
                .toUriString();
    }
}