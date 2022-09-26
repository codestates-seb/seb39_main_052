package com.seb39.myfridge.auth.domain;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.seb39.myfridge.auth.util.JwtClaims;
import lombok.Getter;

@Getter
public class AuthenticationToken {

    private String access;
    private final String refresh;

    public AuthenticationToken(String access, String refresh) {
        this.access = access;
        this.refresh = refresh;
    }

    public void changeAccessToken(String newAccessToken){
        this.access = newAccessToken;
    }

    public Long decodeId(){
        return JWT.decode(access)
                .getClaim(JwtClaims.ID).asLong();
    }
}
