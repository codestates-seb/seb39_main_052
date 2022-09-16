package com.seb39.myfridge.auth.enums;

import lombok.Getter;

public enum AuthCookieType {
    REFRESH_TOKEN("refresh-token");

    @Getter
    private final String name;

    AuthCookieType(String name) {
        this.name = name;
    }
}
