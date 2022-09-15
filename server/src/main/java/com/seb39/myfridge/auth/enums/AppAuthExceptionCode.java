package com.seb39.myfridge.auth.enums;

import lombok.Getter;

@Getter
public enum AppAuthExceptionCode {

    DATA_DESERIALIZE_ERROR(401,"Failed to login request body deserialization"),
    REFRESH_TOKEN_EMPTY(401,"Refresh token is empty"),
    INVALID_EMAIL_OR_PASSWORD(401,"Invalid email or password"),
    ACCESS_TOKEN_EXPIRED(401, "Access token expired"),
    REFRESH_TOKEN_EXPIRED(401,"Refresh token expired"),
    INVALID_ACCESS_TOKEN(401,"Invalid access token"),
    REFRESH_TOKEN_NOT_EXIST(401, "Refresh token not exist"),
    UNDEFINED(401, "Undefined");

    private final int code;
    private final String description;

    AppAuthExceptionCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
