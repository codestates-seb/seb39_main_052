package com.seb39.myfridge.member.dto;

import com.seb39.myfridge.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class MemberDto {


    @Getter
    @Setter
    public static class Patch{
        @NotBlank
        private String name;
    }

    @Getter
    @Setter
    public static class Response{
        private Long id;
        private String name;
        private String profileImagePath;

        public Response() {
        }

        public Response(Member member) {
            this.id = member.getId();
            this.name = member.getName();
            this.profileImagePath = member.getProfileImagePath();
        }

        @Override
        public String toString() {
            return "Response{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", profileImagePath='" + profileImagePath + '\'' +
                    '}';
        }
    }

    @Getter
    @Setter
    public static class ResponseDetail{
        private Long id;
        private String name;
        private String profileImagePath;
        private List<String> roles;

        public ResponseDetail() {
        }

        public ResponseDetail(Member member) {
            this.id = member.getId();
            this.name = member.getName();
            this.profileImagePath = member.getProfileImagePath();
            this.roles = member.getRoleList();
        }
    }
}
