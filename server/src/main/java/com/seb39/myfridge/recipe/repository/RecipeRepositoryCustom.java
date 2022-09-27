package com.seb39.myfridge.recipe.repository;


import com.seb39.myfridge.recipe.dto.RecipeDto;
import com.seb39.myfridge.recipe.dto.RecipeSearch;

import java.util.List;

public interface RecipeRepositoryCustom {

    List<String> searchTitles(String title);

    List<RecipeDto.Response> searchRecipes(RecipeSearch recipeSearch);
}
