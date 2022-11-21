package com.seb39.myfridge.domain.recipe.repository;

import com.seb39.myfridge.domain.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> ,RecipeRepositoryCustom{

     @Query("select r from Recipe r join fetch r.member left join fetch r.image left join fetch r.steps s left join fetch s.image where r.id = :id")
     Optional<Recipe> findWithDetails(@Param("id") Long id);

     @Query("select r from Recipe r join fetch r.recipeIngredients ri join fetch ri.ingredient where r.id = :id")
     Optional<Recipe> findWithIngredients(@Param("id") Long id);

     @Modifying
     @Query("update Recipe r set r.view = r.view + 1 where r.id = :id")
     void addView(@Param("id") Long id);
}
