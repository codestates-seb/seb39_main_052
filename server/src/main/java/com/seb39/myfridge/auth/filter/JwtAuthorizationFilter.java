package com.seb39.myfridge.auth.filter;

import com.seb39.myfridge.auth.PrincipalDetails;
import com.seb39.myfridge.auth.domain.AuthenticationToken;
import com.seb39.myfridge.auth.enums.AppAuthExceptionCode;
import com.seb39.myfridge.auth.exception.AppAuthenticationException;
import com.seb39.myfridge.auth.service.AuthenticationTokenService;
import com.seb39.myfridge.auth.util.AuthenticationTokenUtils;
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
    private final AuthenticationTokenService authenticationTokenService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberService memberService, AuthenticationTokenService authenticationTokenService) {
        super(authenticationManager);
        this.memberService = memberService;
        this.authenticationTokenService = authenticationTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 미로그인 상태
        if(request.getHeader(HttpHeaders.AUTHORIZATION) == null){
            chain.doFilter(request,response);
            return;
        }

        AuthenticationToken token = AuthenticationTokenUtils.getTokenFromRequest(request);
        authenticationTokenService.verifyAccessToken(token);

        Long memberId = token.decodeId();
        if (!memberService.existById(memberId))
            throw new AppAuthenticationException(AppAuthExceptionCode.INVALID_ACCESS_TOKEN);

        PrincipalDetails principal = createPrincipalDetails(memberId);
        savePrincipalInSecurityContext(principal);

        chain.doFilter(request, response);
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