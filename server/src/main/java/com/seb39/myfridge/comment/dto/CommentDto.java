package com.seb39.myfridge.comment.dto;

import lombok.Builder;
import lombok.Data;
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
        private Long memberId;
        private Long recipeId;
        private Long commentId;
        private String content;
        private LocalDateTime createdAt;

        @Builder
        private Response(Long memberId, Long recipeId, Long commentId, String content, LocalDateTime createdAt) {
            this.memberId = memberId;
            this.recipeId = recipeId;
            this.commentId = commentId;
            this.content = content;
            this.createdAt = createdAt;
        }
    }
}
