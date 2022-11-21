package com.seb39.myfridge.domain.ingredient.Repository;

import com.seb39.myfridge.domain.ingredient.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    Optional<List<RecipeIngredient>> findByRecipeId(Long recipeId);

    @Modifying
    @Query("delete from RecipeIngredient r where r.recipe.id = :id")
    void deleteRecipeIngredientByRecipeId(@Param("id") Long id);
}
