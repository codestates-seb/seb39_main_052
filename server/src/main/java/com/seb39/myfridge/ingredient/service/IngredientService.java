package com.seb39.myfridge.ingredient.service;

import com.seb39.myfridge.ingredient.Repository.IngredientRepository;
import com.seb39.myfridge.ingredient.Repository.RecipeIngredientRepository;
import com.seb39.myfridge.ingredient.entity.Ingredient;
import com.seb39.myfridge.ingredient.entity.RecipeIngredient;
import com.seb39.myfridge.recipe.entity.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    @Transactional
    public void createIngredient(Recipe recipe, List<RecipeIngredient> recipeIngredients) {
        for (RecipeIngredient recipeIngredient : recipeIngredients) {
            String name = recipeIngredient.getIngredient().getName();
            if (ingredientRepository.findByName(name).isEmpty()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(recipeIngredient.getIngredient().getName());
                ingredientRepository.save(ingredient);
            }

            Ingredient ingredient = ingredientRepository.findByName(name).get();

            RecipeIngredient recipeIngredient1 = new RecipeIngredient();
            recipeIngredient1.setQuantity(recipeIngredient.getQuantity());
            recipeIngredient1.setRecipe(recipe);
            recipeIngredient1.setIngredient(ingredient);
            recipeIngredientRepository.save(recipeIngredient1);

            recipeIngredient1.addRecipe(recipe);
        }
    }

    @Transactional
    public void updateIngredient(Recipe recipe, List<RecipeIngredient> recipeIngredients) {
        recipeIngredientRepository.deleteRecipeIngredientByRecipeId(recipe.getId());
        createIngredient(recipe, recipeIngredients);
    }

    public List<String> findNamesByContainsWord(String word){
        return ingredientRepository.searchNames(word);
    }
}
