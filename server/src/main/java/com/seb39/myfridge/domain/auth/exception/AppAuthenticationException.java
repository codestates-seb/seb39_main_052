package com.seb39.myfridge.domain.auth.exception;

import com.seb39.myfridge.domain.auth.enums.AppAuthExceptionCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

public class AppAuthenticationException extends AuthenticationException {

    @Getter
    private final AppAuthExceptionCode exceptionCode;

    public AppAuthenticationException(AppAuthExceptionCode exceptionCode, String msg){
        super(msg);
        this.exceptionCode = exceptionCode;
    }

    public AppAuthenticationException(String msg) {
        this(AppAuthExceptionCode.UNDEFINED,msg);
    }

    public AppAuthenticationException(AppAuthExceptionCode exceptionCode) {
        this(exceptionCode,exceptionCode.getDescription());
    }
}
