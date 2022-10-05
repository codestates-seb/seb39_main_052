package com.seb39.myfridge.recipe.service;

import com.seb39.myfridge.image.entity.Image;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.mapper.RecipeMapper;
import com.seb39.myfridge.recipe.repository.RecipeRepository;
import com.seb39.myfridge.step.entity.Step;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RecipeServiceTest {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private RecipeMapper recipeMapper;

    // @Test
    public void 레시피등록_테스트() {
        //given
        Recipe recipe = new Recipe();
        recipe.setTitle("Test title!");
        recipe.setView(0);
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setLastModifiedAt(LocalDateTime.now());

        List<Step> steps = new ArrayList<>();

        Step step = new Step();
        step.setContent("물을 끓인다");
        step.setSequence(1);
        steps.add(step);

        Step step2 = new Step();
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
                .email("test1@naver.com")
                .password("1234")
                .buildGeneralMember();
        memberService.signUpGeneral(member);

        //when
        Recipe savedRecipe = recipeService.createRecipe(recipe, steps, member.getId(), new ArrayList<>(), new ArrayList<>());
        System.out.println(recipe);

        //then
        assertEquals(savedRecipe.getId(), recipe.getId());
        assertEquals(savedRecipe.getMember().getEmail(), member.getEmail());
    }

    // @Test
    public void 레시피수정_테스트() {
        //given
        Image image = new Image();
        image.setIsUpdated("N");
        image.setIdx(0);
        image.setImagePath("imagePath");

        Recipe recipe = new Recipe();
        recipe.setTitle("Test title!");
        recipe.setView(0);
        recipe.setImage(image);
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setLastModifiedAt(LocalDateTime.now());

        List<Step> steps = new ArrayList<>();

        Step step = new Step();
        step.setContent("물을 끓인다");
        step.setSequence(1);
        step.setImage(image);
        steps.add(step);

        Step step2 = new Step();
        step2.setContent("스프를 넣는다");
        step2.setSequence(2);
        step2.setImage(image);
        steps.add(step2);

        Member member = Member.generalBuilder()
                .name("nameA")
                .email("test2@naver.com")
                .password("1234")
                .buildGeneralMember();
        memberService.signUpGeneral(member);

        Recipe savedRecipe = recipeService.createRecipe(recipe, steps, member.getId(), new ArrayList<>(), new ArrayList<>());
        //when
        String updateTitle = "Update title!!";
        savedRecipe.setTitle(updateTitle);
        Recipe updateRecipe = recipeService.updateRecipe(savedRecipe, new ArrayList<>(), member.getId(), new ArrayList<>(), new ArrayList<>());
//        System.out.println(updateRecipe.getTitle());

        //then
        assertEquals(updateRecipe.getTitle(), updateTitle);
        assertEquals(updateRecipe.getMember().getId(), member.getId());

    }

    @Test
    public void 레시피삭제_테스트() {
        //given
        Image image = new Image();
        image.setIsUpdated("N");
        image.setIdx(0);
        image.setImagePath("imagePath");

        Recipe recipe = new Recipe();
        recipe.setTitle("Test title!");
        recipe.setView(0);
        recipe.setImage(image);
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setLastModifiedAt(LocalDateTime.now());

        Member member = Member.generalBuilder()
                .name("nameA")
                .email("test@naver.com")
                .password("1234")
                .buildGeneralMember();
        memberService.signUpGeneral(member);

        Recipe savedRecipe = recipeService.createRecipe(recipe, new ArrayList<>(), member.getId(), new ArrayList<>(), new ArrayList<>());
        //when
        recipeService.deleteRecipe(savedRecipe.getId(),member.getId());
        //then
        assertEquals(0, recipeRepository.findAll().size());
    }

    @Test
    @DisplayName("관리자 계정일 경우 자신이 작성하지 않았더라도 레시피를 삭제할 수 있다.")
    public void removeRecipe_admin() {
        //given
        Image image = new Image();
        image.setIsUpdated("N");
        image.setIdx(0);
        image.setImagePath("imagePath");

        Recipe recipe = new Recipe();
        recipe.setTitle("Test title!");
        recipe.setView(0);
        recipe.setImage(image);
        recipe.setCreatedAt(LocalDateTime.now());
        recipe.setLastModifiedAt(LocalDateTime.now());

        Member admin = Member.generalBuilder()
                .name("admin")
                .email("admin@naver.com")
                .password("1234")
                .buildGeneralMember();
        ReflectionTestUtils.setField(admin,"roles","ROLE_ADMIN");
        memberService.signUpGeneral(admin);

        Member member = Member.generalBuilder()
                .name("nameA")
                .email("test@naver.com")
                .password("1234")
                .buildGeneralMember();
        memberService.signUpGeneral(member);

        Recipe savedRecipe = recipeService.createRecipe(recipe, new ArrayList<>(), member.getId(), new ArrayList<>(), new ArrayList<>());

        //when
        recipeService.deleteRecipe(savedRecipe.getId(),admin.getId());
        //then
        assertEquals(0, recipeRepository.findAll().size());
    }
}