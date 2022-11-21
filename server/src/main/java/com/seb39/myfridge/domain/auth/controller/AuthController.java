package com.seb39.myfridge.domain.auth.controller;

import com.seb39.myfridge.domain.auth.domain.AuthenticationToken;
import com.seb39.myfridge.domain.auth.dto.AuthResponse;
import com.seb39.myfridge.domain.auth.dto.SignUpRequest;
import com.seb39.myfridge.domain.auth.service.AuthenticationTokenService;
import com.seb39.myfridge.domain.auth.util.AuthenticationTokenUtils;
import com.seb39.myfridge.domain.member.entity.Member;
import com.seb39.myfridge.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final MemberService memberService;
    private final AuthenticationTokenService authenticationTokenService;

    @PostMapping("/api/signup")
    public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest){
        Member member = signUpRequest.toMember();
        memberService.signUpGeneral(member);
        return new ResponseEntity<>(AuthResponse.success(), HttpStatus.CREATED);
    }

    @PostMapping("/api/auth/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request, HttpServletResponse response){
        AuthenticationToken token = AuthenticationTokenUtils.getTokenFromRequest(request);
        authenticationTokenService.refresh(token);
        AuthenticationTokenUtils.addTokenInResponse(response,token);
        return ResponseEntity.ok(AuthResponse.success());
    }
}
