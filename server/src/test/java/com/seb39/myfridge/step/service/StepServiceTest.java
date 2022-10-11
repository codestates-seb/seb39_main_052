package com.seb39.myfridge.step.service;

import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.service.RecipeService;
import com.seb39.myfridge.step.entity.Step;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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