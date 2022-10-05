package com.seb39.myfridge.recipe.repository;


import com.seb39.myfridge.ingredient.entity.Ingredient;
import com.seb39.myfridge.recipe.dto.MyRecipeDto;
import com.seb39.myfridge.recipe.dto.RecipeDto;
import com.seb39.myfridge.recipe.dto.RecipeRecommendDto;
import com.seb39.myfridge.recipe.dto.RecipeSearch;
import com.seb39.myfridge.recipe.enums.RecipeSort;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RecipeRepositoryCustom {

    List<String> searchTitles(String title);

    Page<RecipeSearch.Response> searchRecipes(RecipeSearch.Request request);

    Page<MyRecipeDto.Mine> findMyRecipes(Long memberId, int page, RecipeSort sort);

    Page<MyRecipeDto.Favorite> findFavoriteRecipes(Long memberId, int page);


    List<RecipeRecommendDto> findPopularRecipes();

    List<RecipeRecommendDto> findRecentRecipes();

    List<RecipeRecommendDto> recommendByIngredientNames(List<String> ingredientNames);

}
