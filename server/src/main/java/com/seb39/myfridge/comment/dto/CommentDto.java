package com.seb39.myfridge.comment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


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

        @Builder
        private Response(Long memberId, Long recipeId, Long commentId, String content) {
            this.memberId = memberId;
            this.recipeId = recipeId;
            this.commentId = commentId;
            this.content = content;
        }
    }
}
