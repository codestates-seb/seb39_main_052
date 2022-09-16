package com.seb39.myfridge.auth.exception;

import com.seb39.myfridge.auth.enums.AppAuthExceptionCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

public class AppAuthenticationException extends AuthenticationException {

    @Getter
    private final AppAuthExceptionCode exceptionCode;

    public AppAuthenticationException(String msg) {
        super(msg);
        this.exceptionCode = AppAuthExceptionCode.UNDEFINED;
    }

    public AppAuthenticationException(AppAuthExceptionCode exceptionCode){
        super(exceptionCode.getDescription());
        this.exceptionCode = exceptionCode;
    }
}
