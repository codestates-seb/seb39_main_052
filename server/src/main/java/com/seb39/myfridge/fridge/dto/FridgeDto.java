package com.seb39.myfridge.fridge.dto;

import com.seb39.myfridge.ingredient.entity.Ingredient;
import com.seb39.myfridge.recipe.dto.RecipeDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class FridgeDto {

    @Getter
    @NoArgsConstructor
    public static class Post{
        private List<IngredientInfo> fridgeIngredients;

        @Builder
        public Post(List<IngredientInfo> fridgeIngredients) {
            this.fridgeIngredients = fridgeIngredients;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class IngredientInfoResponse{
        private Long id;
        private String name;
        private String quantity;
        private LocalDate expiration;
        private String note;

        @Builder
        public IngredientInfoResponse(Long id, String name, String quantity, LocalDate expiration, String note) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.expiration = expiration;
            this.note = note;
        }
    }



    @Getter
    @NoArgsConstructor
    public static class IngredientInfo {
        private String name;
        private String quantity;
        private LocalDate expiration;
        private String note;

        @Builder
        public IngredientInfo(String name, String quantity, LocalDate expiration, String note) {
            this.name = name;
            this.quantity = quantity;
            this.expiration = expiration;
            this.note = note;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response{
        private List<IngredientInfoResponse> fridgeIngredients;

        @Builder
        public Response(List<IngredientInfoResponse> fridgeIngredients) {
            this.fridgeIngredients = fridgeIngredients;
        }
    }
}
