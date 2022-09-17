package com.seb39.myfridge.recipe.service;

import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.mapper.RecipeMapper;
import com.seb39.myfridge.recipe.repository.RecipeRepository;
import com.seb39.myfridge.step.entity.Step;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RecipeServiceTest {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private RecipeMapper recipeMapper;

    @Test
    public void 레시피등록_테스트() {
        //given
        Recipe recipe = new Recipe();
        recipe.setTitle("Test title!");
        recipe.setView(0);
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setLastModifiedAt(LocalDateTime.now());

        List<Step> steps = new ArrayList<>();

        Step step = new Step();
        step.setTitle("라면 만들기 1단계");
        step.setContent("물을 끓인다");
        step.setSequence(1);
        steps.add(step);

        Step step2 = new Step();
        step2.setTitle("라면 만들기 2단계");
        step2.setContent("스프를 넣는다");
        step2.setSequence(2);
        steps.add(step2);

        Recipe recipe1 = new Recipe();
        recipe1.setTitle("recipe2");
        recipe1.setView(0);
        recipe1.setCreatedAt(LocalDateTime.now());
        recipe1.setLastModifiedAt(LocalDateTime.now());

        Member member = Member.generalBuilder()
                .name("nameA")
                .email("test@naver.com")
                .password("1234")
                .buildGeneralMember();
        memberService.signUpGeneral(member);
        
        //when
        Recipe savedRecipe = recipeService.createRecipe(recipe, steps, member.getId());
        System.out.println(recipe);

        //then
        assertEquals(savedRecipe.getId(), recipe.getId());
        assertEquals(savedRecipe.getMember().getEmail(), member.getEmail());
    }

    @Test
    public void 레시피수정_테스트() {
        //given
        Recipe recipe = new Recipe();
        recipe.setTitle("Test title!");
        recipe.setView(0);
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setLastModifiedAt(LocalDateTime.now());

        List<Step> steps = new ArrayList<>();

        Step step = new Step();
        step.setTitle("라면 만들기 1단계");
        step.setContent("물을 끓인다");
        step.setSequence(1);
        steps.add(step);

        Step step2 = new Step();
        step2.setTitle("라면 만들기 2단계");
        step2.setContent("스프를 넣는다");
        step2.setSequence(2);
        steps.add(step2);

        Member member = Member.generalBuilder()
                .name("nameA")
                .email("test@naver.com")
                .password("1234")
                .buildGeneralMember();
        memberService.signUpGeneral(member);

        Recipe savedRecipe = recipeService.createRecipe(recipe, steps, member.getId());
        //when
        String updateTitle = "Update title!!";
        savedRecipe.setTitle(updateTitle);
        Recipe updateRecipe = recipeService.updateRecipe(savedRecipe, new ArrayList<>(), 1L);
//        System.out.println(updateRecipe.getTitle());

        //then
        assertEquals(updateRecipe.getTitle(), updateTitle);
        assertEquals(updateRecipe.getMember().getId(), member.getId());

    }

    @Test
    public void 레시피삭제_테스트() {
        //given
        Recipe recipe = new Recipe();
        recipe.setTitle("Test title!");
        recipe.setView(0);
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setLastModifiedAt(LocalDateTime.now());

        Recipe savedRecipe = recipeService.createRecipe(recipe, new ArrayList<>(), 1L);
        //when
        recipeService.deleteRecipe(savedRecipe.getId(),1L);
        //then
        assertEquals(0, recipeRepository.findAll().size());
    }

}