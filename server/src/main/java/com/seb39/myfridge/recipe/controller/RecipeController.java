package com.seb39.myfridge.recipe.controller;


import com.seb39.myfridge.dto.SingleResponseDto;
import com.seb39.myfridge.auth.PrincipalDetails;
import com.seb39.myfridge.heart.service.HeartService;
import com.seb39.myfridge.ingredient.entity.RecipeIngredient;
import com.seb39.myfridge.auth.annotation.AuthMemberId;
import com.seb39.myfridge.ingredient.entity.RecipeIngredient;
import com.seb39.myfridge.recipe.dto.RecipeDto;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.mapper.RecipeMapper;
import com.seb39.myfridge.recipe.service.RecipeService;
import com.seb39.myfridge.step.entity.Step;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final HeartService heartService;
    private final RecipeMapper recipeMapper;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<RecipeDto.ResponseDetail> postRecipe(@Valid @RequestPart RecipeDto.Post requestBody,
                                                               @RequestPart List<MultipartFile> files,
                                                               @AuthMemberId Long memberId) {
        //1. 이미지 관련 exception 처리 필요

        List<RecipeIngredient> recipeIngredients = recipeMapper.ingredientsDtoToIngredients(requestBody.getIngredients());

        List<Step> stepList = recipeMapper.recipeDtoStepsToStepList(requestBody.getSteps());
        Recipe recipe = recipeMapper.recipePostToRecipe(requestBody);

        Recipe savedRecipe = recipeService.createRecipe(recipe, stepList, memberId, files, recipeIngredients);
        RecipeDto.ResponseDetail response = recipeMapper.recipeToRecipeResponse(savedRecipe);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteRecipe(@PathVariable("id") @Positive Long id,
                                       @AuthMemberId Long memberId) {

        recipeService.deleteRecipe(id, memberId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RecipeDto.ResponseDetail> updateRecipe(@PathVariable("id") @Positive Long id,
                                                                 @Valid @RequestPart RecipeDto.Patch requestBody,
                                                                 @RequestPart List<MultipartFile> files,
                                                                 @AuthMemberId Long memberId){        requestBody.setId(id);
        List<Step> stepList = recipeMapper.recipeDtoStepsToStepListForPatch(requestBody.getSteps());
        List<RecipeIngredient> recipeIngredients = recipeMapper.ingredientsDtoToIngredients(requestBody.getIngredients());
        Recipe recipe = recipeService.updateRecipe(recipeMapper.recipePatchToRecipe(requestBody), stepList, memberId, files, recipeIngredients);
        RecipeDto.ResponseDetail response = recipeMapper.recipeToRecipeResponse(recipe);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeDto.ResponseDetail> findRecipe(@PathVariable("id") @Positive Long id) {
        Recipe recipe = recipeService.findRecipeWithDetails(id);
        int heartCounts = heartService.findHeartCounts(id);
        RecipeDto.ResponseDetail response = recipeMapper.recipeToRecipeResponse(recipe,heartCounts);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/titles")
    public ResponseEntity<SingleResponseDto<List<String>>> findTitles(@RequestParam String word){
        List<String> titles = recipeService.findTitlesByContainsWord(word);
        return ResponseEntity.ok(new SingleResponseDto<>(titles));
    }

}