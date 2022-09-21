package com.seb39.myfridge.recipe.controller;

import com.seb39.myfridge.auth.PrincipalDetails;
import com.seb39.myfridge.recipe.dto.RecipeDto;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.mapper.RecipeMapper;
import com.seb39.myfridge.recipe.service.RecipeService;
import com.seb39.myfridge.step.entity.Step;
import com.seb39.myfridge.step.service.StepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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


    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity postRecipe(@Valid @RequestPart RecipeDto.Post requestBody,
                                     @RequestPart List<MultipartFile> files,
                                     @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMemberId();
        //1. 이미지 관련 exception 처리 필요

        List<Step> stepList = recipeMapper.recipeDtoStepsToStepList(requestBody.getSteps());
        Recipe recipe = recipeMapper.recipePostToRecipe(requestBody);

        Recipe savedRecipe = recipeService.createRecipe(recipe, stepList, memberId, files);
        RecipeDto.Response response = recipeMapper.recipeToRecipeResponse(savedRecipe);

        return new ResponseEntity(response, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteRecipe(@PathVariable("id") @Positive Long id,
                                       @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Long memberId = principalDetails.getMemberId();
        recipeService.deleteRecipe(id, memberId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity updateRecipe(@PathVariable("id") @Positive Long id,
                                       @Valid @RequestPart RecipeDto.Patch requestBody,
                                       @RequestPart List<MultipartFile> files,
                                       @AuthenticationPrincipal PrincipalDetails principalDetails){
        requestBody.setId(id);
        for (MultipartFile file : files) {
            System.out.println("file name : " + file.getName());
        }
        Long memberId = principalDetails.getMemberId();
        List<Step> stepList = recipeMapper.recipeDtoStepsToStepList(requestBody.getSteps());
        Recipe recipe = recipeService.updateRecipe(recipeMapper.recipePatchToRecipe(requestBody), stepList, memberId, files);
        RecipeDto.Response response = recipeMapper.recipeToRecipeResponse(recipe);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity findRecipe(@PathVariable("id") @Positive Long id) {
        Recipe recipe = recipeService.findRecipeById(id);

        RecipeDto.Response response = recipeMapper.recipeToRecipeResponse(recipe);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    /**
     * 이미지 수정
     * 원래 db에 저장되어 있던 imagePath와 달라짐
     * 만약 3번 step에 해당하는 이미지를 사용자가 수정한다 ?
     *  메인, 1번, 2번, 4번에는 db에 저장된 것과 같은 imagePath가 요청으로 올 것임
     *  3번만 다른 imagePath가 저장될것
     */
}