package com.seb39.myfridge.recipe.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        HEART;
    }
}
