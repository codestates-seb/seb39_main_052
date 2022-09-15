package com.seb39.myfridge.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.seb39.myfridge.auth.enums.AppAuthExceptionCode;
import com.seb39.myfridge.auth.enums.AuthCookieType;
import com.seb39.myfridge.auth.enums.JwtClaim;
import com.seb39.myfridge.auth.enums.JwtTokenType;
import com.seb39.myfridge.auth.exception.AppAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import java.util.*;

@Slf4j
@Service
public class JwtService {

    @Value("${app.auth.jwt.secret}")
    private String secret;

    @Value("${app.auth.jwt.expiration-time-millis.access-token}")
    private long accessExpirationTimeMillis;

    @Value("${app.auth.jwt.expiration-time-millis.refresh-token}")
    private long refreshExpirationTimeMillis;

    private final Map<String,String> repository = new HashMap<>();


    public String createAccessToken(Long id, String email) {
        return JWT.create()
                .withSubject(JwtTokenType.ACCESS.getSubject())
                .withExpiresAt(getExpiredDate(accessExpirationTimeMillis))
                .withClaim(JwtClaim.ID.toString(),id)
                .withClaim(JwtClaim.EMAIL.toString(), email)
                .sign(Algorithm.HMAC512(secret));
    }

    public String createNewAccessToken(String prevAccessToken, String refreshToken){
        verifyRefreshToken(refreshToken);
        verifyTokenPair(prevAccessToken, refreshToken);

        Long id = JWT.decode(prevAccessToken)
                .getClaim(JwtClaim.ID.toString())
                .asLong();
        String email = JWT.decode(prevAccessToken)
                .getClaim(JwtClaim.EMAIL.toString())
                .asString();

        String newAccessToken = createAccessToken(id, email);
        saveToken(refreshToken,newAccessToken);
        return newAccessToken;
    }

    private void verifyRefreshToken(String refreshToken){
        try{
            JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(refreshToken);
        }catch(TokenExpiredException e){
            repository.remove(refreshToken);
            throw new AppAuthenticationException(AppAuthExceptionCode.REFRESH_TOKEN_EXPIRED);
        }
    }

    private void verifyTokenPair(String accessToken, String refreshToken){
        log.info("repository = {}",repository);
        log.info("access = {}, refresh = {}",accessToken, refreshToken);

        String savedAccessToken = Optional.ofNullable(repository.get(refreshToken))
                .orElseThrow(() -> new AppAuthenticationException(AppAuthExceptionCode.INVALID_ACCESS_TOKEN));

        if(!savedAccessToken.equals(accessToken))
            throw new AppAuthenticationException(AppAuthExceptionCode.INVALID_ACCESS_TOKEN);
    }

    public String createRefreshToken(String accessToken){
        String refreshToken =  JWT.create()
                .withSubject(JwtTokenType.REFRESH.getSubject())
                .withExpiresAt(getExpiredDate(refreshExpirationTimeMillis))
                .sign(Algorithm.HMAC512(secret));

        saveToken(refreshToken,accessToken);
        return refreshToken;
    }

    private Date getExpiredDate(long expirationTime) {
        return new Date(System.currentTimeMillis() + expirationTime);
    }

    public String decodeJwtTokenAndGetEmail(String jwtToken){
        return JWT.require(Algorithm.HMAC512(secret))
                .build()
                .verify(jwtToken)
                .getClaim(JwtClaim.EMAIL.toString())
                .asString();
    }

    private void saveToken(String refreshToken, String accessToken){
        repository.put(refreshToken,accessToken);
    }


    public boolean isJwtAccessTokenHeader(String header) {
        return StringUtils.hasText(header) && header.startsWith("Bearer");
    }

    public String accessTokenToAuthorizationHeader(String token){
        return "Bearer " + token;
    }

    public String authorizationHeaderToAccessToken(String authorizationHeader) {
        return authorizationHeader.replace("Bearer ", "");
    }


    public void removeRefreshToken(String token){
        repository.remove(token);
    }

    public Optional<String> takeRefreshToken(Cookie[] cookies){
        if(cookies == null){
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(AuthCookieType.REFRESH_TOKEN.getName()))
                .map(Cookie::getValue)
                .findAny();
    }
}
