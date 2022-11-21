package com.seb39.myfridge.domain.auth.userinfo;

import java.util.Map;
import java.util.Optional;

public class GoogleUserInfo implements OAuth2UserInfo{

    private String username;
    private String email;
    private String providerId;
    private String profileImagePath;

    public GoogleUserInfo(Map<String,Object> attributes) {
        Optional.ofNullable(attributes.get("name"))
                .ifPresent(name-> this.username = name.toString());
        Optional.ofNullable(attributes.get("email"))
                .ifPresent(email-> this.email = email.toString());
        Optional.ofNullable(attributes.get("sub"))
                .ifPresent(sub-> this.providerId = sub.toString());
        Optional.ofNullable(attributes.get("picture"))
                .ifPresent(picture-> this.profileImagePath = picture.toString());
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
        return "google";
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
