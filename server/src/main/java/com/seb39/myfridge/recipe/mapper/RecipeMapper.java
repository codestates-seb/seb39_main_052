package com.seb39.myfridge.recipe.mapper;

import com.seb39.myfridge.ingredient.entity.Ingredient;
import com.seb39.myfridge.ingredient.entity.RecipeIngredient;
import com.seb39.myfridge.recipe.dto.RecipeDto;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.step.entity.Step;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecipeMapper {

    default List<Step> recipeDtoStepsToStepList(List<RecipeDto.Step> steps) {
        List<Step> stepList = new ArrayList<>();
        for (RecipeDto.Step step : steps) {
            Step step1 = new Step();
            step1.setContent(step.getContent());
            step1.setSequence(step.getSequence());
            step1.setImagePath(step.getImagePath());

            stepList.add(step1);
        }
        return stepList;
    }


    default List<RecipeIngredient> ingredientsDtoToIngredients(List<RecipeDto.Ingredient> ingredients) {
        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        for (RecipeDto.Ingredient ingredient : ingredients) {
            Ingredient ingredient1 = new Ingredient();
            ingredient1.setName(ingredient.getName());

            RecipeIngredient recipeIngredient = new RecipeIngredient();
            recipeIngredient.setQuantity(ingredient.getQuantity());
            recipeIngredient.setIngredient(ingredient1);
            recipeIngredients.add(recipeIngredient);
        }
        return recipeIngredients;
    }

    default RecipeDto.Response recipeToRecipeResponse(Recipe recipe) {
       return RecipeDto.Response.builder()
                .id(recipe.getId())
                .createdAt(recipe.getCreatedAt())
                .lastModifiedAt(recipe.getLastModifiedAt())
                .title(recipe.getTitle())
               .portion(recipe.getPortion())
               .time(recipe.getTime())
                .imagePath(recipe.getImagePath())
               .ingredients(ingredientsToDto(recipe.getRecipeIngredients()))
                .steps(stepsToDto(recipe.getSteps()))
                .member(recipe.getMember())
                .build();
    }

//    Recipe recipePostToRecipe(RecipeDto.Post requestBody);

    Recipe recipePatchToRecipe(RecipeDto.Patch requestBody);

    default Recipe recipePostToRecipe(RecipeDto.Post requestBody) {
        Recipe recipe = new Recipe();
        recipe.setTitle(requestBody.getTitle());
        recipe.setImagePath(requestBody.getImagePath());
        recipe.setPortion(requestBody.getPortion());
        recipe.setTime(requestBody.getTime());

        return recipe;
    }

    default List<RecipeDto.Ingredient> ingredientsToDto(List<RecipeIngredient> ingredients) {
        List<RecipeDto.Ingredient> ingredientList = new ArrayList<>();

        for (RecipeIngredient ingredient : ingredients) {
            RecipeDto.Ingredient build = RecipeDto.Ingredient.builder()
                    .name(ingredient.getIngredient().getName())
                    .quantity(ingredient.getQuantity())
                    .build();
            ingredientList.add(build);
        }
        return ingredientList;
    }

    default List<RecipeDto.Step> stepsToDto(List<Step> steps) {
        return steps.stream()
                .map(this::stepToDto)
                .collect(Collectors.toList());
    }

//    RecipeDto.Step stepToDto(Step step);

    default RecipeDto.Step stepToDto(Step step) {
        return RecipeDto.Step.builder()
                .content(step.getContent())
                .imagePath(step.getImagePath())
                .sequence(step.getSequence())
                .build();

    }

}
