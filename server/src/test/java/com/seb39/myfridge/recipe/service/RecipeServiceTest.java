package com.seb39.myfridge.recipe.service;

import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RecipeServiceTest {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    public void 레시피등록_테스트() {
        //given
        Recipe recipe = new Recipe();
        recipe.setTitle("Test title!");
        recipe.setView(0);
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setLastModifiedAt(LocalDateTime.now());

        //when
        Recipe savedRecipe = recipeService.createRecipe(recipe);

        //then
        assertEquals(savedRecipe.getId(), recipe.getId());
    }

    @Test
    public void 레시피수정_테스트() {
        //given
        Recipe recipe = new Recipe();
        recipe.setTitle("Test title!");
        recipe.setView(0);
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setLastModifiedAt(LocalDateTime.now());

        Recipe savedRecipe = recipeService.createRecipe(recipe);
        //when
        String updateTitle = "Update title!!";
        savedRecipe.setTitle(updateTitle);
        Recipe updateRecipe = recipeService.updateRecipe(savedRecipe);
//        System.out.println(updateRecipe.getTitle());

        //then
        assertEquals(recipeRepository.findById(savedRecipe.getId()).get().getTitle(), updateTitle);
    }

    @Test
    public void 레시피삭제_테스트() {
        //given
        Recipe recipe = new Recipe();
        recipe.setTitle("Test title!");
        recipe.setView(0);
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setLastModifiedAt(LocalDateTime.now());

        Recipe savedRecipe = recipeService.createRecipe(recipe);
        //when
        recipeService.deleteRecipe(savedRecipe.getId());
        //then
        assertEquals(0, recipeRepository.findAll().size());
    }

}