package com.seb39.myfridge.recipe.repository;

import com.seb39.myfridge.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

     @Query("select r from Recipe r join fetch r.member join fetch r.steps where r.id = :id")
     Optional<Recipe> findWithMemberAndSteps(@Param("id") Long id);

     @Query("select r from Recipe r join fetch r.recipeIngredients where r.id = :id")
     Optional<Recipe> findWithIngredients(@Param("id") Long id);
}
