package com.seb39.myfridge.auth.controller;

import com.seb39.myfridge.auth.PrincipalDetails;
import com.seb39.myfridge.auth.dto.AuthResponse;
import com.seb39.myfridge.auth.dto.SignUpRequest;
import com.seb39.myfridge.auth.enums.AppAuthExceptionCode;
import com.seb39.myfridge.auth.exception.AppAuthenticationException;
import com.seb39.myfridge.auth.service.JwtService;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final JwtService jwtService;

    @PostMapping("/api/signup")
    public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest){
        Member member = signUpRequest.toMember();
        memberService.signUpGeneral(member);
        return new ResponseEntity<>(AuthResponse.success(), HttpStatus.CREATED);
    }

    @GetMapping("/api/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletRequest request, HttpServletResponse response){
        jwtService.takeRefreshToken(request.getCookies())
                        .ifPresent(jwtService::removeRefreshToken);
        return ResponseEntity.ok(AuthResponse.success());
    }

    @PostMapping("/api/auth/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request, HttpServletResponse response){
        String accessTokenHeader = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .orElseThrow(() -> new AppAuthenticationException(AppAuthExceptionCode.ACCESS_TOKEN_NOT_EXIST));
        String accessToken = jwtService.authorizationHeaderToAccessToken(accessTokenHeader);

        String refreshToken = jwtService.takeRefreshToken(request.getCookies())
                .orElseThrow(() -> new AppAuthenticationException(AppAuthExceptionCode.REFRESH_TOKEN_NOT_EXIST));

        String newAccessToken = jwtService.createNewAccessToken(accessToken, refreshToken);
        String headerValue = jwtService.accessTokenToAuthorizationHeader(newAccessToken);
        response.setHeader(HttpHeaders.AUTHORIZATION,headerValue);
        return ResponseEntity.ok(AuthResponse.success());
    }

    /**
     * 인증 테스트를 위한 Method
     */
    @GetMapping("/api/authtest")
    @Secured("ROLE_USER")
    public String authTest(@AuthenticationPrincipal PrincipalDetails principal){
        return principal.getMemberId() + " / " + principal.getUsername();
    }


}