package com.seb39.myfridge.auth.dto;

import com.seb39.myfridge.auth.enums.AppAuthExceptionCode;
import lombok.Getter;

@Getter
public class AuthResponse {

    private final boolean success;
    private final int code;
    private final String failureReason;

    private AuthResponse(boolean success, int code, String failureReason) {
        this.success = success;
        this.code = code;
        this.failureReason = failureReason;
    }

    public static AuthResponse success(){
        return new AuthResponse(true, 0,"");
    }

    public static AuthResponse failure(String reason){
        return new AuthResponse(false, 0, reason);
    }

    public static AuthResponse failure(AppAuthExceptionCode exceptionCode){
        return new AuthResponse(false,exceptionCode.getCode(), exceptionCode.getDescription());
    }
}
