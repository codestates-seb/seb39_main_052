package com.seb39.myfridge.recipe.service;

import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.repository.RecipeRepository;
import com.seb39.myfridge.step.entity.Step;
import com.seb39.myfridge.step.repository.StepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final StepRepository stepRepository;

    @Transactional
    public Recipe createRecipe(Recipe recipe, List<Step> steps) {
        //변경해야 할 사항
        //1. 레시피를 등록한 member 값 저장
        //2. 레시피를 등록할 때 입력한 재료 저장

        steps.forEach(step -> step.addRecipe(recipe));
        Recipe savedRecipe = recipeRepository.save(recipe);
        stepRepository.saveAll(steps);
        return savedRecipe;
    }

    @Transactional
    public Recipe updateRecipe(Recipe recipe, List<Step> steps) {
        Recipe findRecipe = findRecipeById(recipe.getId());

        //수정하는 사람이 recipe 작성자인지 검색하는 로직 필요
        Optional.ofNullable(recipe.getTitle()).ifPresent(findRecipe::setTitle);

        if(!findRecipe.getSteps().isEmpty()) {
            findRecipe.getSteps().clear();
        }
        steps.forEach(step -> step.addRecipe(findRecipe));
        Recipe updateRecipe = recipeRepository.save(findRecipe);

        stepRepository.saveAll(steps);

        return updateRecipe;
    }

    @Transactional
    public void deleteRecipe(long id) {
        Recipe findRecipe = findRecipeById(id);
        //삭제하는 사람이 recipe 작성자인지 검색하는 로직 필요
        recipeRepository.delete(findRecipe);
    }

    public Recipe findRecipeById(Long recipeId) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        return optionalRecipe.orElseThrow(() -> new IllegalArgumentException("해당 레시피를 찾을 수 없습니다."));
    }


}
