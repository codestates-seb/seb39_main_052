package com.seb39.myfridge.recipe.mapper;

import com.seb39.myfridge.recipe.dto.RecipeDto;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.step.entity.Step;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecipeMapper {


    //List<Step> recipeDtoStepsToStepList(List<RecipeDto.Step> steps);
    //requestBody에 담긴 List를 step으로 변환하여 저장하는 로직 필요

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

    default RecipeDto.Response recipeToRecipeResponse(Recipe recipe) {
       return RecipeDto.Response.builder()
                .id(recipe.getId())
                .createdAt(recipe.getCreatedAt())
                .lastModifiedAt(recipe.getLastModifiedAt())
                .title(recipe.getTitle())
               .portion(recipe.getPortion())
               .time(recipe.getTime())
                .imagePath(recipe.getImagePath())
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
