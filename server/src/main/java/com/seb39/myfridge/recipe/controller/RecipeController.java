package com.seb39.myfridge.recipe.controller;

import com.seb39.myfridge.recipe.dto.RecipeDto;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.mapper.RecipeMapper;
import com.seb39.myfridge.recipe.service.RecipeService;
import com.seb39.myfridge.step.entity.Step;
import com.seb39.myfridge.step.service.StepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final StepService stepService;
    private final RecipeService recipeService;
    private final RecipeMapper recipeMapper;

    @PostMapping
    public ResponseEntity postRecipe(@Valid @RequestBody RecipeDto.Post requestBody) {
        List<Step> stepList = recipeMapper.recipeDtoStepsToStepList(requestBody.getSteps());
        Recipe recipe = recipeMapper.recipePostToRecipe(requestBody);

        Recipe savedRecipe = recipeService.createRecipe(recipe, stepList);
        RecipeDto.Response response = recipeMapper.recipeToRecipeResponse(savedRecipe);

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity findRecipe(@PathVariable("id") @Positive Long id) {
        Recipe recipe = recipeService.findRecipeById(id);

        RecipeDto.Response response = recipeMapper.recipeToRecipeResponse(recipe);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    //update 구현
    @PatchMapping("/{id}")
    public ResponseEntity updateRecipe(@PathVariable("id") @Positive Long id,
                                       @Valid @RequestBody RecipeDto.Patch requestBody) {
        requestBody.setId(id);
        List<Step> stepList = recipeMapper.recipeDtoStepsToStepList(requestBody.getSteps());
        Recipe recipe = recipeService.updateRecipe(recipeMapper.recipePatchToRecipe(requestBody), stepList);
        RecipeDto.Response response = recipeMapper.recipeToRecipeResponse(recipe);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteRecipe(@PathVariable("id") @Positive Long id) {
        recipeService.deleteRecipe(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}