package com.seb39.myfridge.recipe.repository;

import com.seb39.myfridge.config.QueryDslConfig;
import com.seb39.myfridge.image.entity.Image;
import com.seb39.myfridge.ingredient.Repository.IngredientRepository;
import com.seb39.myfridge.ingredient.Repository.RecipeIngredientRepository;
import com.seb39.myfridge.ingredient.entity.Ingredient;
import com.seb39.myfridge.ingredient.entity.RecipeIngredient;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.repository.MemberRepository;
import com.seb39.myfridge.recipe.dto.RecipeDto;
import com.seb39.myfridge.recipe.dto.RecipeSearch;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.step.entity.Step;
import com.seb39.myfridge.step.repository.StepRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@DataJpaTest
@Transactional
@Import(QueryDslConfig.class)
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
        ingredientRepository.saveAll(List.of(ingredient1, ingredient2));

        Image image = new Image();
        image.setImagePath("https://s3.aws..abcdefg.com/dog.jpeg");

        Recipe recipe = new Recipe();
        recipe.setTitle("RECIPE 01");
        recipe.setMember(member);
        recipe.setImage(image);
        for (int i = 1; i <= 3; i++) {
            Step step = new Step();
            step.setSequence(i);
            step.setContent("Step " + i);
            step.addRecipe(recipe);

            Image stepImage = new Image();
            stepImage.setImagePath("https://s3.aws..abcdefg.com/step" + i + ".jpeg");
            step.setImage(stepImage);
        }

        RecipeIngredient ri1 = new RecipeIngredient();
        ri1.addRecipe(recipe);
        ri1.addIngredient(ingredient1);
        ri1.setQuantity("조금");

        RecipeIngredient ri2 = new RecipeIngredient();
        ri2.addRecipe(recipe);
        ri2.addIngredient(ingredient2);
        ri2.setQuantity("많이");

        recipe.setRecipeIngredients(List.of(ri1, ri2));

        recipeRepository.save(recipe);

        em.flush();
        em.clear();
        System.out.println("------------------------------------");

        // when
        Recipe findRecipe = recipeRepository.findWithDetails(recipe.getId()).get();
        System.out.println("id = " + findRecipe.getId());
        System.out.println("title = " + findRecipe.getTitle());
        System.out.println("writer = " + findRecipe.getMember().getName());
        System.out.println("image = " +findRecipe.getImage().getImagePath());

        for (Step step : findRecipe.getSteps()) {
            System.out.println("  step = " + step.getContent());
            System.out.println("  step.imagePath = " + step.getImage().getImagePath());
        }

        recipeRepository.findWithIngredients(recipe.getId());
        for (RecipeIngredient ri : findRecipe.getRecipeIngredients()) {
            System.out.println(" ri = " + ri.getIngredient().getName());
        }
    }

    @Test
    @DisplayName("특정 문자열이 포함된 레시피 제목 리스트 조회. (한글)")
    void searchTitles() throws Exception {
        // given
        for (int i = 0; i <= 21; i++) {
            Recipe recipe = new Recipe();
            if (i % 2 == 0)
                recipe.setTitle(i + " title " + i);
            else
                recipe.setTitle(i + " xxxx " + i);
            recipeRepository.save(recipe);
        }
        recipeRepository.searchTitles("title");

        em.flush();
        em.clear();

        // when
        List<String> titles = recipeRepository.searchTitles("title");

        //then
        Assertions.assertThat(titles.size()).isEqualTo(10);
        Assertions.assertThat(titles)
                .anyMatch(title -> title.contains("title"));
    }

    @Test
    @DisplayName("특정 문자열이 포함된 레시피 제목 리스트 조회. (일치하는 레시피 없음)")
    void searchTitlesNotExist() throws Exception {
        // given
        for (int i = 0; i <= 3; i++) {
            Recipe recipe = new Recipe();
            recipe.setTitle(i + " title " + i);
            recipeRepository.save(recipe);
        }
        recipeRepository.searchTitles("unknown");

        em.flush();
        em.clear();

        // when
        List<String> titles = recipeRepository.searchTitles("unknown");

        //then
        Assertions.assertThat(titles.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("특정 문자열이 포함된 레시피 제목 리스트 조회. (영문 대소문자를 구분하지 않아야 한다)")
    void searchTitlesIgnoreCase() throws Exception {
        // given
        List<String> titles = List.of("Creamy Chicken Penne Pasta",
                "Cheesy Chicken Alfredo Pasta Bake",
                "One Pot Garlic Parmesan Pasta",
                "Penne With Tomato Sauce Pasta");
        for (String title : titles) {
            Recipe recipe = new Recipe();
            recipe.setTitle(title);
            recipeRepository.save(recipe);
        }

        em.flush();
        em.clear();

        // when
        List<String> result = recipeRepository.searchTitles("pasta");

        //then
        Assertions.assertThat(result.size()).isEqualTo(4);
    }

//    @Test
//    @DisplayName("레시피 리스트 검색 테스트")
    void searchRecipesTest() throws Exception {
        // given
        List<String> titles = List.of("Creamy Chicken Penne Pasta",
                "Cheesy Chicken Alfredo Pasta Bake",
                "One Pot Garlic Parmesan Pasta",
                "Penne With Tomato Sauce Pasta");
        Member member = Member.oauth2Builder()
                .name("SJ")
                .profileImagePath("https://s3.aws.abcd/image.jpeg")
                .buildOAuth2Member();
        memberRepository.save(member);

        for (String title : titles) {
            Recipe recipe = new Recipe();
            recipe.setTitle(title);
            recipe.setMember(member);
            recipeRepository.save(recipe);
        }

        em.flush();
        em.clear();

        // when
        RecipeSearch recipeSearch = new RecipeSearch();
        recipeSearch.setTitle("pasta");
        List<RecipeDto.Response> result = recipeRepository.searchRecipes(recipeSearch);
        for (RecipeDto.Response response : result) {
            System.out.println(response);
        }

        //then
        Assertions.assertThat(result.size()).isEqualTo(4);
    }
}