package com.seb39.myfridge.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${app.auth.jwt.secret}")
    private String secret;

    @Value("${app.auth.jwt.expiration-time-millis}")
    private long expirationTimeMillis;

    public String createJwtToken(String email) {
        return JWT.create()
                .withSubject("login jwt token")
                .withExpiresAt(getExpiredDate())
                .withClaim("email", email)
                .sign(Algorithm.HMAC512(secret));
    }

    private Date getExpiredDate() {
        return new Date(System.currentTimeMillis() + expirationTimeMillis);
    }

    public String decodeJwtTokenAndGetEmail(String jwtToken){
        return JWT.require(Algorithm.HMAC512(secret))
                .build()
                .verify(jwtToken)
                .getClaim("email")
                .asString();
    }
}
