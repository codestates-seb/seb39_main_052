package com.seb39.myfridge.auth.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.seb39.myfridge.auth.PrincipalDetails;
import com.seb39.myfridge.auth.enums.AppAuthExceptionCode;
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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
            Long memberId = jwtService.verifyJwtTokenAndGetId(accessToken);

            if (!memberService.existById(memberId))
                throw new AppAuthenticationException(AppAuthExceptionCode.INVALID_ACCESS_TOKEN);

            PrincipalDetails principal = createPrincipalDetails(memberId);
            savePrincipalInSecurityContext(principal);

        } catch (TokenExpiredException e) {
            throw new AppAuthenticationException(AppAuthExceptionCode.ACCESS_TOKEN_EXPIRED);
        }catch(JWTVerificationException e){
            throw new AppAuthenticationException(AppAuthExceptionCode.INVALID_ACCESS_TOKEN);
        }

        chain.doFilter(request,response);
    }
    private PrincipalDetails createPrincipalDetails(Long memberId) {
        Member member = memberService.findById(memberId);
        return PrincipalDetails.general(member);
    }
    private void savePrincipalInSecurityContext(PrincipalDetails principal) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().equals("/api/auth/refresh");
    }
}