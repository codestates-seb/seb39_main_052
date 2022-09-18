package com.seb39.myfridge.auth.controller;

import com.seb39.myfridge.auth.dto.AuthResponse;
import com.seb39.myfridge.auth.dto.SignUpRequest;
import com.seb39.myfridge.auth.service.JwtService;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
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
    private final JwtService jwtService;

    @PostMapping("/api/signup")
    public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest){
        Member member = signUpRequest.toMember();
        memberService.signUpGeneral(member);
        return new ResponseEntity<>(AuthResponse.success(), HttpStatus.CREATED);
    }

    @GetMapping("/api/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletRequest request, HttpServletResponse response){
        jwtService.removeRefreshToken(request,response);
        return ResponseEntity.ok(AuthResponse.success());
    }

    @PostMapping("/api/auth/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpServletRequest request, HttpServletResponse response){
        jwtService.refresh(request,response);
        return ResponseEntity.ok(AuthResponse.success());
    }
}
