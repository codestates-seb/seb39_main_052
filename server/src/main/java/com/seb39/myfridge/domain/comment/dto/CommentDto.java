package com.seb39.myfridge.domain.comment.dto;

import com.seb39.myfridge.domain.member.dto.MemberDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;


public class CommentDto {

    @Getter
    @Setter
    public static class Post{
        @NotBlank
        private String content;
    }

    @Getter
    @Setter
    public static class Patch{
        @NotBlank
        private String content;
    }


    @Getter
    @Setter
    public static class Response{
        private MemberDto.Response member;
        private Long recipeId;
        private Long commentId;
        private String content;
        private LocalDateTime createdAt;

        @Builder
        private Response(MemberDto.Response member, Long recipeId, Long commentId, String content, LocalDateTime createdAt) {
            this.member = member;
            this.recipeId = recipeId;
            this.commentId = commentId;
            this.content = content;
            this.createdAt = createdAt;
        }
    }
}
