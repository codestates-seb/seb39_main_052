package com.seb39.myfridge.recipe.repository;


import com.seb39.myfridge.recipe.dto.RecipeDto;
import com.seb39.myfridge.recipe.dto.RecipeSearch;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RecipeRepositoryCustom {

    List<String> searchTitles(String title);

    Page<RecipeSearch.Response> searchRecipes(RecipeSearch.Request request);
}
