package com.seb39.myfridge.recipe.dto;

import com.seb39.myfridge.ingredient.entity.RecipeIngredient;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.step.entity.Step;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    public static class Response{
        private Long id;
        private String title;
        private int portion;
        private String time;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private String imagePath;
        private List<Ingredient> ingredients;
        private List<Step> steps;
        private Long memberId;
        private String memberName;

        @Builder
        public Response(Long id, String title, int portion, String time, LocalDateTime createdAt, LocalDateTime lastModifiedAt, String imagePath,List<Ingredient> ingredients ,List<Step> steps, Member member) {
            this.id = id;
            this.title = title;
            this.portion = portion;
            this.time = time;
            this.createdAt = createdAt;
            this.lastModifiedAt = lastModifiedAt;
            this.imagePath = imagePath;
            this.ingredients = ingredients;
            this.steps = steps;
            this.memberId = member.getId();
            this.memberName = member.getName();
        }
    }
}
