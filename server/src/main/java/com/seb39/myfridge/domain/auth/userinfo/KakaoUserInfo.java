package com.seb39.myfridge.domain.auth.userinfo;

import java.util.Map;
import java.util.Optional;

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

    private String username;
    private String providerId;
    private String email;
    private String profileImagePath;

    public KakaoUserInfo(Map<String,Object> attributes) {
        Map<String,Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String,Object> profile = (Map<String, Object>) account.get("profile");

        Optional.ofNullable(attributes.get("id"))
                .ifPresent(id -> this.providerId = id.toString());
        Optional.ofNullable(account.get("email"))
                .ifPresent(email -> this.email = email.toString());
        Optional.ofNullable(profile.get("nickname"))
                .ifPresent(nickname -> this.username = nickname.toString());
        Optional.ofNullable(profile.get("thumbnail_image_url"))
                .ifPresent(url -> this.profileImagePath = url.toString());
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

    @Override
    public String getProfileImagePath() {
        return profileImagePath;
    }
}
