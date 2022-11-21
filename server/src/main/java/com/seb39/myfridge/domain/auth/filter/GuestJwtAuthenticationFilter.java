package com.seb39.myfridge.domain.auth.filter;


import com.seb39.myfridge.domain.auth.service.AuthenticationTokenService;
import com.seb39.myfridge.domain.member.entity.Member;
import com.seb39.myfridge.domain.member.service.MemberService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class GuestJwtAuthenticationFilter extends JwtAuthenticationFilter{

    private final MemberService memberService;

    public GuestJwtAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationTokenService authenticationTokenService, MemberService memberService) {
        super(authenticationManager, authenticationTokenService);
        this.memberService = memberService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        Member guestMember = memberService.signUpGuest();

        String email = guestMember.getEmail();
        String password = "guestpassword";

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManager.authenticate(token);
    }
}
