package com.seb39.myfridge.auth.enums;

import lombok.Getter;

@Getter
public enum AppAuthExceptionCode {

    ACCESS_TOKEN_EXPIRED(1, "Access token expired"),
    INVALID_ACCESS_TOKEN(2,"Invalid access token"),
    ACCESS_TOKEN_NOT_EXIST(3,"Access token not exist"),
    REFRESH_TOKEN_EXPIRED(4,"Refresh token expired"),
    INVALID_REFRESH_TOKEN(5,"Refresh token is empty"),
    REFRESH_TOKEN_NOT_EXIST(6, "Refresh token not exist"),
    INVALID_EMAIL_OR_PASSWORD(7,"Invalid email or password"),
    DATA_DESERIALIZE_ERROR(8,"Failed to login request body deserialization"),
    EXISTS_EMAIL(9,"Email already exists"),
    UNDEFINED(99, "Undefined");

    private final int code;
    private final String description;

    AppAuthExceptionCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
