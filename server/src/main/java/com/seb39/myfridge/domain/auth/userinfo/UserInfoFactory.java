package com.seb39.myfridge.domain.auth.userinfo;


import java.util.Map;
import java.util.Optional;

public class UserInfoFactory {

    public static Optional<OAuth2UserInfo> create(String provider, Map<String,Object> attributes){
        switch(provider.toUpperCase()){
            case "GOOGLE":
                return Optional.of(new GoogleUserInfo(attributes));
            case "KAKAO":
                return Optional.of(new KakaoUserInfo(attributes));
            default:
                return Optional.empty();
        }
    }
}
