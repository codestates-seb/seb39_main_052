package com.seb39.myfridge.auth.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.seb39.myfridge.auth.domain.AuthenticationToken;
import com.seb39.myfridge.auth.util.JwtClaims;
import com.seb39.myfridge.auth.enums.JwtTokenType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthenticationTokenProvider {

    @Value("${app.auth.jwt.secret}")
    private String secret;

    @Value("${app.auth.jwt.expiration-time-millis.access-token}")
    private long accessExpirationTimeMillis;

    @Value("${app.auth.jwt.expiration-time-millis.refresh-token}")
    private long refreshExpirationTimeMillis;

    public AuthenticationToken createToken(Long id) {
        String access = createAccessToken(id);
        String refresh = createRefreshToken(id);
        return new AuthenticationToken(access,refresh);
    }

    public void renew(AuthenticationToken token){
        Long id = token.decodeId();
        String newAccessToken = createAccessToken(id);
        token.changeAccessToken(newAccessToken);
    }

    private String createAccessToken(Long id){
        return JWT.create()
                .withSubject(JwtTokenType.ACCESS.getSubject())
                .withExpiresAt(getExpiredDate(accessExpirationTimeMillis))
                .withClaim(JwtClaims.ID,id)
                .sign(Algorithm.HMAC512(secret));
    }

    private String createRefreshToken(Long id){
        return JWT.create()
                .withSubject(JwtTokenType.REFRESH.getSubject())
                .withExpiresAt(getExpiredDate(refreshExpirationTimeMillis))
                .withClaim(JwtClaims.ID,id)
                .sign(Algorithm.HMAC512(secret));
    }

    private Date getExpiredDate(long expirationTime) {
        return new Date(System.currentTimeMillis() + expirationTime);
    }
}
