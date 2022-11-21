package com.seb39.myfridge.domain.recipe.mapper;

import com.seb39.myfridge.domain.recipe.entity.Recipe;
import com.seb39.myfridge.domain.image.entity.Image;
import com.seb39.myfridge.domain.ingredient.entity.Ingredient;
import com.seb39.myfridge.domain.ingredient.entity.RecipeIngredient;
import com.seb39.myfridge.domain.member.dto.MemberDto;
import com.seb39.myfridge.domain.recipe.dto.RecipeDto;
import com.seb39.myfridge.domain.step.entity.Step;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    default List<Step> recipeDtoStepsToStepListForPatch(List<RecipeDto.Step> steps) {
        List<Step> stepList = new ArrayList<>();
        for (RecipeDto.Step step : steps) {
            Step step1 = new Step();
            step1.setContent(step.getContent());
            step1.setSequence(step.getSequence());
            step1.setImage(imageDtoToImage(step.getImageInfo()));

            stepList.add(step1);
        }
        return stepList;
    }

    default List<Step> recipeDtoStepsToStepList(List<RecipeDto.Step> steps) {
        List<Step> stepList = new ArrayList<>();
        for (RecipeDto.Step step : steps) {
            Step step1 = new Step();
            step1.setContent(step.getContent());
            step1.setSequence(step.getSequence());
            System.out.println(step.getImageInfo().getImagePath());
            step1.setImage(imageDtoToImage(step.getImageInfo()));
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

    default RecipeDto.ResponseDetail recipeToRecipeResponseDetail(Recipe recipe, int heartCounts, boolean heartExist) {
        return RecipeDto.ResponseDetail.builder()
                .id(recipe.getId())
                .createdAt(recipe.getCreatedAt())
                .lastModifiedAt(recipe.getLastModifiedAt())
                .title(recipe.getTitle())
                .portion(recipe.getPortion())
                .time(recipe.getTime())
                .imageInfo(imageToDto(recipe.getImage()))
                .view(recipe.getView())
                .ingredients(ingredientsToDto(recipe.getRecipeIngredients()))
                .steps(stepsToDto(recipe.getSteps()))
                .member(new MemberDto.Response(recipe.getMember()))
                .heartCounts(heartCounts)
                .heartExist(heartExist)
                .build();
    }

    default RecipeDto.ResponseDetail recipeToRecipeResponseDetail(Recipe recipe) {
        return recipeToRecipeResponseDetail(recipe,0, false);
    }

    default Recipe recipePatchToRecipe(RecipeDto.Patch requestBody) {
        Recipe recipe = new Recipe();
        recipe.setId(requestBody.getId());
        recipe.setTitle(requestBody.getTitle());
        recipe.setPortion(requestBody.getPortion());
        recipe.setTime(requestBody.getTime());
        recipe.setSteps(recipeDtoStepsToStepList(requestBody.getSteps()));
        recipe.setImage(imageDtoToImage(requestBody.getImageInfo()));
        return recipe;
    }

    default Recipe recipePostToRecipe(RecipeDto.Post requestBody) {
        Recipe recipe = new Recipe();
        recipe.setTitle(requestBody.getTitle());
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

    default RecipeDto.Step stepToDto(Step step) {
        return RecipeDto.Step.builder()
                .content(step.getContent())
                .imageInfo(imageToDto(step.getImage()))
                .sequence(step.getSequence())
                .build();
    }

    default Image imageDtoToImage(RecipeDto.ImageInfo imageInfo) {
        Image image = new Image();
        image.setImagePath(imageInfo.getImagePath());
        image.setIdx(imageInfo.getIdx());
        image.setIsUpdated(imageInfo.getIsUpdated());
        return image;
    }

    default RecipeDto.ImageInfo imageToDto(Image image) {
        return RecipeDto.ImageInfo.builder()
                .idx(image.getIdx())
                .imagePath(image.getImagePath())
                .isUpdated(image.getIsUpdated())
                .build();
    }
}
