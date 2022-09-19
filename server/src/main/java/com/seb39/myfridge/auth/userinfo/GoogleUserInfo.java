package com.seb39.myfridge.auth.userinfo;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo{

    private final String username;
    private final String email;
    private final String providerId;

    public GoogleUserInfo(Map<String,Object> attributes) {
        this.username = attributes.get("name").toString();
        this.email = attributes.get("email").toString();
        this.providerId = attributes.get("sub").toString();
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
}