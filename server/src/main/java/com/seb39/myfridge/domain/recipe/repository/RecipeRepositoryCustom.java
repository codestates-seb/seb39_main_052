package com.seb39.myfridge.domain.recipe.repository;


import com.seb39.myfridge.domain.recipe.dto.RecipeSearch;
import com.seb39.myfridge.domain.recipe.enums.RecipeSort;
import com.seb39.myfridge.domain.recipe.dto.MyRecipeDto;
import com.seb39.myfridge.domain.recipe.dto.RecipeRecommendDto;
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
