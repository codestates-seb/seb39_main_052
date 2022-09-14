package com.seb39.myfridge.recipe.repository;

import com.seb39.myfridge.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

}
