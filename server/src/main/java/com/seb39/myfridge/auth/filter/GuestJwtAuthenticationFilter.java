package com.seb39.myfridge.auth.filter;


import com.seb39.myfridge.auth.PrincipalDetails;
import com.seb39.myfridge.auth.domain.AuthenticationToken;
import com.seb39.myfridge.auth.dto.LoginRequest;
import com.seb39.myfridge.auth.dto.LoginResponse;
import com.seb39.myfridge.auth.service.AuthenticationTokenService;
import com.seb39.myfridge.auth.util.AuthenticationTokenUtils;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


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
