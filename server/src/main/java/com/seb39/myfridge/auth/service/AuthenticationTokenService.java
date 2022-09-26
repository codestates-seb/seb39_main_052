package com.seb39.myfridge.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seb39.myfridge.auth.domain.AuthenticationToken;
import com.seb39.myfridge.auth.enums.AppAuthExceptionCode;
import com.seb39.myfridge.auth.exception.AppAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationTokenService {

    @Value("${app.auth.jwt.secret}")
    private String secret;

    private final Map<String, String> repository = new HashMap<>();

    private final AuthenticationTokenProvider tokenProvider;

    private ObjectMapper om = new ObjectMapper();

    public AuthenticationToken issueAuthenticationToken(Long memberId) {
        AuthenticationToken token = tokenProvider.createToken(memberId);
        saveToken(token);
        return token;
    }

    public void refresh(AuthenticationToken token) {
        verifyRefreshToken(token.getRefresh());
        verifyTokenPair(token);
        tokenProvider.renew(token);
        saveToken(token);
    }

    public void verifyAccessToken(AuthenticationToken token){
        try{
            JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.getAccess());
        }catch (TokenExpiredException e) {
            throw new AppAuthenticationException(AppAuthExceptionCode.ACCESS_TOKEN_EXPIRED);
        }catch(JWTVerificationException e){
            throw new AppAuthenticationException(AppAuthExceptionCode.INVALID_ACCESS_TOKEN);
        }
    }

    public void removeToken(AuthenticationToken token){
        repository.remove(token.getRefresh());
    }

    private void verifyRefreshToken(String refreshToken) {
        if(refreshToken == null)
            throw new AppAuthenticationException(AppAuthExceptionCode.REFRESH_TOKEN_NOT_EXIST);

        try {
            JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(refreshToken);
        } catch (TokenExpiredException e) {
            repository.remove(refreshToken);
            throw new AppAuthenticationException(AppAuthExceptionCode.REFRESH_TOKEN_EXPIRED);
        }
    }

    private void verifyTokenPair(AuthenticationToken token) {
        String accessToken = token.getAccess();
        String refreshToken = token.getRefresh();

        String savedAccessToken = Optional.ofNullable(repository.get(refreshToken))
                .orElseThrow(() -> new AppAuthenticationException(AppAuthExceptionCode.INVALID_ACCESS_TOKEN));

        if (!savedAccessToken.equals(accessToken))
            throw new AppAuthenticationException(AppAuthExceptionCode.INVALID_ACCESS_TOKEN);
    }

    private void saveToken(AuthenticationToken token) {
        repository.put(token.getRefresh(), token.getAccess());
    }
}
