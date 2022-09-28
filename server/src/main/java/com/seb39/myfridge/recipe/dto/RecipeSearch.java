package com.seb39.myfridge.recipe.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RecipeSearch {
    private String title = "";
    private List<String> ingredients = new ArrayList<>();
    private int page = 1;
    private SortType sortType = SortType.RECENT;

    public enum SortType{
        VIEW,
        RECENT,
        HEART
    }
}
