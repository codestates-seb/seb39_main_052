package com.seb39.myfridge.recipe.dto;

import com.seb39.myfridge.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public class RecipeDto {

    @Getter
    public static class Post{
        @NotBlank(message = "레시피 제목은 공백이 아니어야 합니다.")
        private String title;

        private String imagePath;

        private int portion;

        private String time;

        private List<Ingredient> ingredients;

        private List<Step> steps;

        @Builder
        public Post(String title, String imagePath, int portion, String time, List<Step> steps, List<Ingredient> ingredients) {
            this.title = title;
            this.imagePath = imagePath;
            this.portion = portion;
            this.time = time;
            this.ingredients = ingredients;
            this.steps = steps;
        }
    }

    @Getter
    @Builder
    public static class Patch{

        private Long id;
        @NotBlank(message = "수정할 레시피 제목은 공백이 아니어야 합니다.")
        private String title;

        private String imagePath;

        private int portion;

        private String time;

        private List<Ingredient> ingredients;

        private List<Step> steps;


        public Patch(Long id, String title, String imagePath, int portion, String time, List<Ingredient> ingredients, List<Step> steps) {
            this.id = id;
            this.title = title;
            this.imagePath = imagePath;
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
    public static class Step{
        private int sequence;
        private String content;
        private String imagePath;

        @Builder
        public Step(int sequence, String content, String imagePath) {
            this.sequence = sequence;
            this.content = content;
            this.imagePath = imagePath;
        }
    }

    @Getter
    public static class Ingredient{
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
        private String imagePath;
        private List<Ingredient> ingredients;
        private List<Step> steps;
        private Long memberId;
        private String memberName;
        private int heartCounts;

        @Builder
        public ResponseDetail(Long id, String title, int portion, int view, String time, LocalDateTime createdAt, LocalDateTime lastModifiedAt, String imagePath, List<Ingredient> ingredients , List<Step> steps, Member member, int heartCounts) {
            this.id = id;
            this.title = title;
            this.portion = portion;
            this.view = view;
            this.time = time;
            this.createdAt = createdAt;
            this.lastModifiedAt = lastModifiedAt;
            this.imagePath = imagePath;
            this.ingredients = ingredients;
            this.steps = steps;
            this.memberId = member.getId();
            this.memberName = member.getName();
            this.heartCounts = heartCounts;
        }
    }

    @Getter
    @Setter
    public static class Search{
        private String title;
        private List<String> ingredients;
    }

    @Getter
    @Setter
    public static class Response{
        private Long id;
        private String title;
        private RecipeDto.Response.Member member;
        private String imagePath;
        private int heartCounts;
        private int view;

        @Builder
        private Response(Long id, String title, Long memberId, String memberName, String profileImagePath, String imagePath, int heartCounts, int view) {

            RecipeDto.Response.Member member = new Member();
            member.setId(memberId);
            member.setName(memberName);
            member.setProfileImagePath(profileImagePath);
            this.member = member;

            this.id = id;
            this.title = title;
            this.imagePath = imagePath;
            this.heartCounts = heartCounts;
            this.view = view;
        }

        @Getter
        @Setter
        static class Member{
            private Long id;
            private String name;
            private String profileImagePath;
        }
    }


}
