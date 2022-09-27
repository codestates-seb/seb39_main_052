package com.seb39.myfridge.member.dto;

import com.seb39.myfridge.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

public class MemberDto {

    @Getter
    @Setter
    public static class Response{
        private Long id;
        private String name;
        private String profileImagePath;

        public Response(Member member) {
            this.id = member.getId();
            this.name = member.getName();
            this.profileImagePath = member.getProfileImagePath();
        }
    }
}
