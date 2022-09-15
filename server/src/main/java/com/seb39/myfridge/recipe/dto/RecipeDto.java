package com.seb39.myfridge.recipe.dto;

import com.seb39.myfridge.step.entity.Step;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public class RecipeDto {

    @Getter
    @Setter
    public static class Post{
        @NotBlank(message = "레시피 제목은 공백이 아니어야 합니다.")
        private String title;

        private String imagePath;

        private List<Step> steps;

        @Builder
        public Post(String title, String imagePath, List<Step> steps) {
            this.title = title;
            this.imagePath = imagePath;
            this.steps = steps;
        }
    }

    @Getter
    @Setter
    public static class Step{
//        private Long id;
        private int sequence;
        private String title;
        private String content;
        private String imagePath;

        @Builder
        public Step(Long id, int sequence, String title, String content, String imagePath) {
//            this.id = id;
            this.sequence = sequence;
            this.title = title;
            this.content = content;
            this.imagePath = imagePath;
        }
    }

    @Getter
    @Setter
    public static class Response{
        private Long id;
        private String title;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private List<Step> steps;

        @Builder
        public Response(Long id, String title, LocalDateTime createdAt, LocalDateTime lastModifiedAt, List<Step> steps) {
            this.id = id;
            this.title = title;
            this.createdAt = createdAt;
            this.lastModifiedAt = lastModifiedAt;
            this.steps = steps;
        }
    }
}
