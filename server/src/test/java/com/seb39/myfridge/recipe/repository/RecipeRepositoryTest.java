package com.seb39.myfridge.recipe.repository;

import com.seb39.myfridge.ingredient.Repository.IngredientRepository;
import com.seb39.myfridge.ingredient.Repository.RecipeIngredientRepository;
import com.seb39.myfridge.ingredient.entity.Ingredient;
import com.seb39.myfridge.ingredient.entity.RecipeIngredient;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.repository.MemberRepository;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.step.entity.Step;
import com.seb39.myfridge.step.repository.StepRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RecipeRepositoryTest {

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RecipeIngredientRepository recipeIngredientRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    StepRepository stepRepository;

    @Autowired
    EntityManager em;


    @Test
    void findWithDetailTest() throws Exception {
        // given
        Member member = Member.generalBuilder()
                .name("SJ")
                .buildGeneralMember();
        memberRepository.save(member);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("ingredient 1");
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("ingredient 2");
        ingredientRepository.saveAll(List.of(ingredient1,ingredient2));

        Recipe recipe = new Recipe();
        recipe.setMember(member);
        for(int i=1;i<=3;i++){
            Step step = new Step();
            step.setSequence(i);
            step.setContent("Step "+i);
            step.addRecipe(recipe);
        }

        RecipeIngredient ri1 = new RecipeIngredient();
        ri1.addRecipe(recipe);
        ri1.addIngredient(ingredient1);
        ri1.setQuantity("조금");

        RecipeIngredient ri2 = new RecipeIngredient();
        ri2.addRecipe(recipe);
        ri2.addIngredient(ingredient2);
        ri2.setQuantity("많이");

        recipe.setRecipeIngredients(List.of(ri1,ri2));

        recipeRepository.save(recipe);

        em.flush();
        em.clear();
        System.out.println("------------------------------------");

        // when
        Recipe findRecipe = recipeRepository.findWithMemberAndSteps(recipe.getId()).get();
        System.out.println("id = " + findRecipe.getId());
        System.out.println("title = " + findRecipe.getTitle());
        System.out.println("writer = " + findRecipe.getMember().getName());

        for (Step step : findRecipe.getSteps()) {
            System.out.println("  step = " + step.getContent());
        }

        recipeRepository.findWithIngredients(recipe.getId());
        for (RecipeIngredient ri : findRecipe.getRecipeIngredients()) {
            System.out.println(" ri = " + ri.getIngredient().getName());
        }
    }
}