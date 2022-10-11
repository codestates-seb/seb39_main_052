package com.seb39.myfridge.auth.userinfo;

public interface OAuth2UserInfo {
    String getUsername();
    String getEmail();
    String getProvider();
    String getProviderId();
    String getProfileImagePath();
}