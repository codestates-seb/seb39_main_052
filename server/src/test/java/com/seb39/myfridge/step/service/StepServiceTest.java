package com.seb39.myfridge.step.service;

import com.seb39.myfridge.domain.recipe.service.RecipeService;
import com.seb39.myfridge.domain.step.service.StepService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StepServiceTest {

    @Autowired
    private StepService stepService;

    @Autowired
    private RecipeService recipeService;



    @Test
    public void 단계저장_테스트() {

    }
}