package com.seb39.myfridge.auth.userinfo;

import java.util.Map;

/**
 *  Kakao OAuth2 attributes
 *  {
 *      id=2377708417,
 *      connected_at=2022-08-06T13:34:47Z,
 *      properties={
 *              nickname=성진
 *          },
 *      kakao_account=
 *          {
 *              profile_nickname_needs_agreement=false,
 *              profile=
 *                  {
 *                      nickname=성진
 *                  },
 *              has_email=true,
 *              email_needs_agreement=false,
 *              is_email_valid=true,
 *              is_email_verified=true,
 *              email=skrek269@naver.com
 *          }
 *  }
 *
 */

public class KakaoUserInfo implements OAuth2UserInfo{

    private final String username;
    private final String email;
    private final String providerId;

    public KakaoUserInfo(Map<String,Object> attributes) {
        this.providerId = attributes.get("id").toString();

        Map<String,Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String,Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        this.username = profile.get("nickname").toString();
        this.email = kakaoAccount.get("email").toString();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return providerId;
    }
}
