package com.seb39.myfridge.recipe.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.seb39.myfridge.member.dto.MemberDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public class RecipeDto {

    @Getter
    public static class Post {
        @NotBlank(message = "레시피 제목은 공백이 아니어야 합니다.")
        private String title;
        private int portion;
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
    public static class Patch {
        private Long id;
        @NotBlank(message = "수정할 레시피 제목은 공백이 아니어야 합니다.")
        private String title;
        private ImageInfo imageInfo;
        private int portion;
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

        @Builder
        public ResponseDetail(Long id, String title, int portion, int view, String time, LocalDateTime createdAt, LocalDateTime lastModifiedAt, ImageInfo imageInfo, List<Ingredient> ingredients , List<Step> steps, MemberDto.Response member, int heartCounts) {
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
        }
    }

    @Getter
    @Setter
    public static class SearchResponse {
        private Long id;
        private String title;
        private MemberDto.Response member;
        private String imagePath;
        private int heartCounts;
        private int view;
        private LocalDateTime lastModifiedAt;
        private boolean heartExist = false;

        public SearchResponse() {
        }

        @QueryProjection
        public SearchResponse(Long id, String title, Long memberId, String memberName, String memberProfileImagePath, String imagePath, int heartCounts, int view, LocalDateTime lastModifiedAt) {
            MemberDto.Response member = new MemberDto.Response();
            member.setId(memberId);
            member.setName(memberName);
            member.setProfileImagePath(memberProfileImagePath);

            this.id = id;
            this.title = title;
            this.member = member;
            this.imagePath = imagePath;
            this.heartCounts = heartCounts;
            this.view = view;
            this.lastModifiedAt = lastModifiedAt;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", member=" + member.getName() +
                    ", imagePath='" + imagePath + '\'' +
                    ", heartCounts=" + heartCounts +
                    ", view=" + view +
                    '}';
        }
    }
}
