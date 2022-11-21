package com.seb39.myfridge.domain.auth.dto;

import com.seb39.myfridge.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SignUpRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank(message="password는 필수 입력 항목입니다.")
    private String password;

    @NotBlank(message = "name은 필수 입력 항목입니다.")
    private String name;

    public Member toMember(){
        return Member.generalBuilder()
                .email(email)
                .password(password)
                .name(name)
                .buildGeneralMember();
    }
}
