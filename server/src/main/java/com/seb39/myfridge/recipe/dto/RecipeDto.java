package com.seb39.myfridge.recipe.dto;


import lombok.*;
import com.seb39.myfridge.member.dto.MemberDto;
import org.hibernate.validator.constraints.Range;


import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public class RecipeDto {

    @Getter
    @NoArgsConstructor
    public static class Post {
        @NotBlank(message = "레시피 제목은 공백이 아니어야 합니다.")
        private String title;
        @Range(min = 1, max = 1000)
        private int portion;
        @NotBlank(message = "요리 시간을 입력해주세요!")
        private String time;
        private List<Ingredient> ingredients;
        private List<Step> steps;

        @Builder
        public Post(String title, int portion, String time, List<Step> steps, List<Ingredient> ingredients) {
            this.title = title;
            this.portion = portion;
            this.time = time;
            this.ingredients = ingredients;
            this.steps = steps;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ImageInfo {
        private int idx;
        private String imagePath;
        private String isUpdated;

        @Builder
        public ImageInfo(int idx, String imagePath, String isUpdated) {
            this.idx = idx;
            this.imagePath = imagePath;
            this.isUpdated = isUpdated;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Patch {
        private Long id;
        @NotBlank(message = "수정할 레시피 제목은 공백이 아니어야 합니다.")
        private String title;
        private ImageInfo imageInfo;

        @Range(min = 1, max = 1000)
        private int portion;
        @NotBlank(message = "요리 시간을 입력해주세요!")
        private String time;
        private List<Ingredient> ingredients;
        private List<Step> steps;

        @Builder
        public Patch(Long id, String title, ImageInfo imageInfo, int portion, String time, List<Ingredient> ingredients, List<Step> steps) {
            this.id = id;
            this.title = title;
            this.imageInfo = imageInfo;
            this.portion = portion;
            this.time = time;
            this.ingredients = ingredients;
            this.steps = steps;
        }

        public void setId(long id) {
            this.id = id;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Step {

        private int sequence;
        private String content;
        private ImageInfo imageInfo;

        @Builder
        public Step(int sequence, String content, ImageInfo imageInfo) {
            this.sequence = sequence;
            this.content = content;
            this.imageInfo = imageInfo;
        }

    }

    @Getter
    @NoArgsConstructor
    public static class Ingredient {

        private String name;
        private String quantity;

        @Builder
        public Ingredient(String name, String quantity) {
            this.name = name;
            this.quantity = quantity;
        }
    }

    @Getter
    @Setter
    public static class ResponseDetail {
        private Long id;
        private String title;
        private int portion;
        private int view;
        private String time;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private ImageInfo imageInfo;
        private List<Ingredient> ingredients;
        private List<Step> steps;
        private MemberDto.Response member;
        private int heartCounts;
        private boolean heartExist;

        @Builder
        private ResponseDetail(Long id, String title, int portion, int view, String time, LocalDateTime createdAt, LocalDateTime lastModifiedAt, ImageInfo imageInfo, List<Ingredient> ingredients , List<Step> steps, MemberDto.Response member, int heartCounts, boolean heartExist) {
            this.id = id;
            this.title = title;
            this.portion = portion;
            this.view = view;
            this.time = time;
            this.createdAt = createdAt;
            this.lastModifiedAt = lastModifiedAt;
            this.imageInfo = imageInfo;
            this.ingredients = ingredients;
            this.steps = steps;
            this.member = member;
            this.heartCounts = heartCounts;
            this.heartExist = heartExist;
        }
    }



}
