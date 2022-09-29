package com.seb39.myfridge.ingredient.service;

import com.seb39.myfridge.ingredient.Repository.IngredientRepository;
import com.seb39.myfridge.ingredient.entity.Ingredient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IngredientServiceTest {

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    public void 재료찾기_테스트() {
        //given
        Ingredient ingredient = new Ingredient();
        ingredient.setName("돼지고기");

        ingredientRepository.save(ingredient);
        //when
        Boolean isExist = ingredientService.isIngredientExist("돼지고기");
        Boolean isExist2 = ingredientService.isIngredientExist("불고기");
        //then
        Assertions.assertEquals(isExist, true);
        Assertions.assertEquals(isExist2, false);

    }
}