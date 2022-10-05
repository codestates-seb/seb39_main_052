package com.seb39.myfridge.recipe.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RecipeRecommendDto {

    private Long id;
    private String title;
    private String imagePath;

    @QueryProjection
    public RecipeRecommendDto(Long id, String title, String imagePath) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
    }
}

