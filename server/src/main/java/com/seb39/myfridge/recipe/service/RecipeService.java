package com.seb39.myfridge.recipe.service;

import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Transactional
    public Recipe createRecipe(Recipe recipe) {
        //변경해야 할 사항
        //1. 레시피를 등록한 member 값 저장
        //2. 레시피를 등록할 때 입력한 재료 저장
        Recipe savedRecipe = recipeRepository.save(recipe);
        return savedRecipe;
    }

    @Transactional
    public Recipe updateRecipe(Recipe recipe) {
        Recipe findRecipe = findRecipe(recipe.getId());

        //수정하는 사람이 recipe 작성자인지 검색하는 로직 필요
        Optional.ofNullable(recipe.getTitle()).ifPresent(findRecipe::setTitle);

        Recipe savedRecipe = recipeRepository.save(findRecipe);
        return savedRecipe;
    }

    @Transactional
    public void deleteRecipe(long id) {
        Recipe findRecipe = findRecipe(id);
        //삭제하는 사람이 recipe 작성자인지 검색하는 로직 필요
        recipeRepository.delete(findRecipe);
    }

    public Recipe findRecipe(Long recipeId) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        return optionalRecipe.orElseThrow(() -> new IllegalArgumentException("해당 레시피를 찾을 수 없습니다."));
    }



}
