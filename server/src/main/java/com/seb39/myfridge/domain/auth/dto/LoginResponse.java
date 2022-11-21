package com.seb39.myfridge.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    private Long memberId;

    public LoginResponse(Long memberId) {
        this.memberId = memberId;
    }
}
