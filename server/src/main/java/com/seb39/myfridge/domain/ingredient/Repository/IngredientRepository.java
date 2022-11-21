package com.seb39.myfridge.domain.ingredient.Repository;

import com.seb39.myfridge.domain.ingredient.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long>, IngredientRepositoryCustom {

    Optional<Ingredient> findByName(String name);

}
