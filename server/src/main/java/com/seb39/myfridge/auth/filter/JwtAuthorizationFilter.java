package com.seb39.myfridge.auth.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.seb39.myfridge.auth.PrincipalDetails;
import com.seb39.myfridge.auth.enums.AppAuthExceptionCode;
import com.seb39.myfridge.auth.enums.AuthCookieType;
import com.seb39.myfridge.auth.exception.AppAuthenticationException;
import com.seb39.myfridge.auth.service.JwtService;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final MemberService memberService;
    private final JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberService memberService, JwtService jwtService) {
        super(authenticationManager);
        this.memberService = memberService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!jwtService.isJwtAccessTokenHeader(header)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String accessToken = jwtService.authorizationHeaderToAccessToken(header);
            String email = jwtService.decodeJwtTokenAndGetEmail(accessToken);

            if (StringUtils.hasText(email) && memberService.exist(email)) {
                Member member = memberService.findByEmail(email);
                PrincipalDetails principal = new PrincipalDetails(member);
                Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (TokenExpiredException e) {
            throw new AppAuthenticationException(AppAuthExceptionCode.ACCESS_TOKEN_EXPIRED);
        }catch(JWTVerificationException e){
            throw new AppAuthenticationException(AppAuthExceptionCode.UNDEFINED);
        }

        chain.doFilter(request,response);
    }
}