package com.seb39.myfridge.member.dto;

import lombok.Getter;
import lombok.Setter;

public class MemberDto {

    @Getter
    @Setter
    public static class Response{
        private String name;
        private String profileImagePath;
    }
}
