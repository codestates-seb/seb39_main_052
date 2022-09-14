package com.seb39.myfridge.auth.dto;

import lombok.Getter;

@Getter
public class AuthResponse {

    private final boolean success;
    private final String failureReason;

    private AuthResponse(boolean success, String failureReason) {
        this.success = success;
        this.failureReason = failureReason;
    }

    public static AuthResponse success(){
        return new AuthResponse(true,"");
    }

    public static AuthResponse failure(String reason){
        return new AuthResponse(false,reason);
    }
}
