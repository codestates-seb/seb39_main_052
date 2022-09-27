package com.seb39.myfridge.recipe.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecipeSearch {
    private String title;
    private List<String> ingredients;
    private int page;
    private SortBy sort;

    enum SortBy{
        RECENT,
        HEART,
        VIEW,
    }
}
