package com.seb39.myfridge.auth.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.seb39.myfridge.auth.util.JwtClaims;
import com.seb39.myfridge.auth.enums.JwtTokenType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtProvider {

    @Value("${app.auth.jwt.secret}")
    private String secret;

    @Value("${app.auth.jwt.expiration-time-millis.access-token}")
    private long accessExpirationTimeMillis;

    @Value("${app.auth.jwt.expiration-time-millis.refresh-token}")
    private long refreshExpirationTimeMillis;

    public String createAccessToken(Long id) {
        return JWT.create()
                .withSubject(JwtTokenType.ACCESS.getSubject())
                .withExpiresAt(getExpiredDate(accessExpirationTimeMillis))
                .withClaim(JwtClaims.ID,id)
                .sign(Algorithm.HMAC512(secret));
    }

    public String createRefreshToken(String accessToken){
        return JWT.create()
                .withSubject(JwtTokenType.REFRESH.getSubject())
                .withExpiresAt(getExpiredDate(refreshExpirationTimeMillis))
                .sign(Algorithm.HMAC512(secret));
    }

    private Date getExpiredDate(long expirationTime) {
        return new Date(System.currentTimeMillis() + expirationTime);
    }
}
