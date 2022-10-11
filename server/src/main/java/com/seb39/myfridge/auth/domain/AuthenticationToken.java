package com.seb39.myfridge.auth.domain;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.seb39.myfridge.auth.util.JwtClaims;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("jwt")
public class AuthenticationToken {

    @Id
    private final String refresh;
    private String access;

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
