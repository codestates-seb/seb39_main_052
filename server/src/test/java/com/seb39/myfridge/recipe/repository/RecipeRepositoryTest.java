package com.seb39.myfridge.recipe.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seb39.myfridge.comment.entity.Comment;
import com.seb39.myfridge.comment.repository.CommentRepository;
import com.seb39.myfridge.config.QueryDslConfig;
import com.seb39.myfridge.heart.entity.Heart;
import com.seb39.myfridge.heart.repository.HeartRepository;
import com.seb39.myfridge.heart.service.HeartService;
import com.seb39.myfridge.image.entity.Image;
import com.seb39.myfridge.ingredient.Repository.IngredientRepository;
import com.seb39.myfridge.ingredient.Repository.RecipeIngredientRepository;
import com.seb39.myfridge.ingredient.entity.Ingredient;
import com.seb39.myfridge.ingredient.entity.RecipeIngredient;
import com.seb39.myfridge.member.dto.MemberDto;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.repository.MemberRepository;
import com.seb39.myfridge.recipe.dto.MyRecipeDto;
import com.seb39.myfridge.recipe.dto.RecipeDto;
import com.seb39.myfridge.recipe.dto.RecipeRecommendDto;
import com.seb39.myfridge.recipe.dto.RecipeSearch;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.enums.RecipeSort;
import com.seb39.myfridge.step.entity.Step;
import com.seb39.myfridge.step.repository.StepRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

import static com.seb39.myfridge.heart.entity.QHeart.*;
import static com.seb39.myfridge.recipe.entity.QRecipe.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Import(QueryDslConfig.class)
class RecipeRepositoryTest {

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    HeartService heartService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RecipeIngredientRepository recipeIngredientRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    @Autowired
    StepRepository stepRepository;

    @Autowired
    HeartRepository heartRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    EntityManager em;

    @Autowired
    JPAQueryFactory queryFactory;

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
        ri1.setQuantity("??????");

        RecipeIngredient ri2 = new RecipeIngredient();
        ri2.addRecipe(recipe);
        ri2.addIngredient(ingredient2);
        ri2.setQuantity("??????");

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
        System.out.println("image = " + findRecipe.getImage().getImagePath());

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
    @DisplayName("?????? ???????????? ????????? ????????? ?????? ????????? ??????. (??????)")
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
        assertThat(titles.size()).isEqualTo(10);
        assertThat(titles)
                .anyMatch(title -> title.contains("title"));
    }

    @Test
    @DisplayName("?????? ???????????? ????????? ????????? ?????? ????????? ??????. (???????????? ????????? ??????)")
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
        assertThat(titles.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("?????? ???????????? ????????? ????????? ?????? ????????? ??????. (?????? ??????????????? ???????????? ????????? ??????)")
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
        assertThat(result.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("????????? ?????? - ?????? ?????? ????????? ??????")
    void searchRecipesTitleTest() throws Exception {
        // given
        initSampleRecipes();
        em.flush();
        em.clear();

        // when
        RecipeSearch.Request request = new RecipeSearch.Request();
        request.setTitle("4");
        request.setSort(RecipeSort.RECENT);

        Page<RecipeSearch.Response> page = recipeRepository.searchRecipes(request);
        List<RecipeSearch.Response> content = page.getContent();

        // then
        content.forEach(dto -> {
            assertThat(dto.getTitle()).contains(request.getTitle());
        });
    }

    @Test
    @DisplayName("????????? ?????? - ?????? ?????? ????????? ??????")
    void searchRecipesIngredientsTest() throws Exception {
        // given
        initSampleRecipes();
        em.flush();
        em.clear();

        // when
        RecipeSearch.Request request = new RecipeSearch.Request();
        request.setIngredients(List.of("ingredient 1", "ingredient 3"));
        Page<RecipeSearch.Response> page = recipeRepository.searchRecipes(request);
        List<RecipeSearch.Response> content = page.getContent();
        // then
        content.stream()
                .map(dto -> dto.getId())
                .forEach(id -> {
                    Recipe recipe = recipeRepository.findById(id).get();
                    List<String> names = recipe.getRecipeIngredients().stream()
                            .map(ri -> ri.getIngredient().getName())
                            .collect(Collectors.toList());
                    assertThat(names).contains(request.getIngredients().toArray(String[]::new));
                });
    }

    @Test
    @DisplayName("????????? ?????? - ?????????")
    void searchRecipesRecentTest() throws Exception {
        // given
        initSampleRecipes();
        em.flush();
        em.clear();

        // when
        RecipeSearch.Request request = new RecipeSearch.Request();
        request.setTitle("recipe");
        request.setSort(RecipeSort.RECENT);
        Page<RecipeSearch.Response> page = recipeRepository.searchRecipes(request);
        List<RecipeSearch.Response> content = page.getContent();

        // then
        assertThat(content).isSortedAccordingTo((o1, o2) -> -o1.getLastModifiedAt().compareTo(o2.getLastModifiedAt()));
    }

    @Test
    @DisplayName("????????? ?????? - ????????? ?????? ???")
    void searchRecipesViewTest() throws Exception {
        // given
        initSampleRecipes();
        em.flush();
        em.clear();

        // when
        RecipeSearch.Request request = new RecipeSearch.Request();
        request.setTitle("recipe");
        request.setSort(RecipeSort.VIEW);

        Page<RecipeSearch.Response> page = recipeRepository.searchRecipes(request);
        List<RecipeSearch.Response> content = page.getContent();

        // then
        assertThat(content).isSortedAccordingTo((o1, o2) -> o2.getView() - o1.getView());
    }

    @Test
    @DisplayName("????????? ?????? - ?????? ?????? ???")
    void searchRecipesHeartCountTest() throws Exception {
        // given
        initSampleRecipes();
        em.flush();
        em.clear();

        // when
        RecipeSearch.Request request = new RecipeSearch.Request();
        request.setTitle("recipe");

        request.setSort(RecipeSort.HEART);
        Page<RecipeSearch.Response> page = recipeRepository.searchRecipes(request);
        List<RecipeSearch.Response> content = page.getContent();

        // then
        assertThat(content).isSortedAccordingTo((o1, o2) -> o2.getHeartCounts() - o1.getHeartCounts());
    }

    @Test
    @DisplayName("????????? ?????? - ?????????")
    void searchRecipesPagingTest() throws Exception {
        // given
        initSampleRecipes();
        em.flush();
        em.clear();

        // when
        RecipeSearch.Request request = new RecipeSearch.Request();
        request.setTitle("recipe");
        request.setPage(2);
        request.setSort(RecipeSort.RECENT);

        Page<RecipeSearch.Response> page = recipeRepository.searchRecipes(request);
        List<RecipeSearch.Response> content = page.getContent();

        // then
        assertThat(page.getNumber() + 1).isEqualTo(2);
        assertThat(page.getSize()).isEqualTo(16);
        assertThat(page.getTotalElements()).isEqualTo(40);
        assertThat(page.getTotalPages()).isEqualTo(3);
    }

    @Test
    @DisplayName("?????? ????????? ????????? ????????? ??????")
    void findMyRecipesTest() throws Exception {
        // given
        initSampleRecipes();
        em.flush();
        em.clear();
        Member member = memberRepository.findAll().get(0);

        // when
        Page<MyRecipeDto.Mine> page = recipeRepository.findMyRecipes(member.getId(), 1, RecipeSort.VIEW);
        List<MyRecipeDto.Mine> content = page.getContent();

        // then
        assertThat(content.size()).isEqualTo(4);
        content.forEach(dto -> {
                    Recipe recipe = recipeRepository.findById(dto.getId()).get();
                    assertThat(recipe.getMember()).isEqualTo(member);
                });
    }

    @Test
    @DisplayName("?????? ?????? ????????? ????????? ??????")
    void findMyFavoriteRecipesTest() throws Exception {
        // given
        initSampleRecipes();
        em.flush();
        em.clear();
        Member member = memberRepository.findAll().get(0);

        // when
        Page<MyRecipeDto.Favorite> page = recipeRepository.findFavoriteRecipes(member.getId(), 1);
        List<MyRecipeDto.Favorite> content = page.getContent();

        // then
        for (MyRecipeDto.Favorite dto : content) {
            System.out.println(dto);
        }

        assertThat(content.size()).isEqualTo(4);
    }

    @Test
    void ???????????????_????????????????????????_?????????() {
        //given
        initSampleRecipesForPopularRecipe();
        //when
        List<RecipeRecommendDto> popularRecipes = recipeRepository.findPopularRecipes();
        //then
        assertThat(popularRecipes.size()).isEqualTo(8);
    }

    @Test
    void ???????????????_????????????????????????_?????????() throws InterruptedException {
        //given
        initSampleRecipesForRecentRecipes();
        //when
        List<RecipeRecommendDto> recentRecipes = recipeRepository.findRecentRecipes();
//        for (RecipeRecommendDto recentRecipe : recentRecipes) {
//            Recipe recipe = recipeRepository.findById(recentRecipe.getId()).get();
//            System.out.println("recipe.getCreatedAt() = " + recipe.getCreatedAt());
//        }
        //then
        assertThat(recentRecipes.size()).isEqualTo(8);
        }
        
    @DisplayName("?????? ????????? ?????? ????????? ????????? ????????? ??????")
    void recommendByIngredientNamesTest() throws Exception {
        // given
        initSampleRecipes();
        em.flush();
        em.clear();

        // when
        List<RecipeRecommendDto> result = recipeRepository.recommendByIngredientNames(List.of("ingredient 1", "ingredient 2"));

        List<Recipe> recipes = result.stream()
                .map(dto -> recipeRepository.findById(dto.getId()).get())
                .collect(Collectors.toList());

        // then
        assertThat(recipes).isSortedAccordingTo((o1, o2) -> o2.getView() - o1.getView());
        assertThat(result.size()).isEqualTo(8);
    }

    private void initSampleRecipes() {
        for (int i = 1; i <= 5; i++) {
            Member member = Member.oauth2Builder()
                    .name("member" + i)
                    .profileImagePath("https://s3.aws.abcd/member" + i + ".jpeg")
                    .buildOAuth2Member();
            memberRepository.save(member);
        }
        List<Member> members = memberRepository.findAll();

        for (int i = 1; i <= 4; i++) {
            Ingredient ingredient = new Ingredient();
            ingredient.setName("ingredient " + i);
            ingredientRepository.save(ingredient);
        }
        List<Ingredient> ingredients = ingredientRepository.findAll();

        for (int i = 1; i <= 40; i++) {
            Recipe recipe = new Recipe();
            recipe.setTitle("Recipe " + i);
            recipe.setMember(members.get(i % 5));

            for (int j = 0; j < (i / 10); j++) {
                RecipeIngredient ri = new RecipeIngredient();
                ri.addRecipe(recipe);
                ri.addIngredient(ingredients.get(j));
                ri.setQuantity("??????");
            }

            Image image = new Image();
            image.setImagePath("https://s3.amazon.abcdefg/" + i + ".jpeg");
            recipe.setImage(image);
            int view = (int) ((Math.random() * 10000));
            recipe.setView(view);
            recipeRepository.save(recipe);

            int heartCount = (int) (Math.random() * 4) + 1;
            for (int j = 0; j < heartCount; j++) {
                heartRepository.save(new Heart(members.get(j % 5), recipe));
            }

            int commentCount = (int) (Math.random() * 3);
            for (int j = 0; j < commentCount; j++) {
                Comment comment = Comment.create("comment", members.get(i % 5), recipe);
                commentRepository.save(comment);
            }
        }
    }
    private void initSampleRecipesForPopularRecipe() {
        for (int i = 1; i <= 5; i++) {
            Member member = Member.oauth2Builder()
                    .name("member" + i)
                    .profileImagePath("https://s3.aws.abcd/member" + i + ".jpeg")
                    .buildOAuth2Member();
            memberRepository.save(member);
        }
        List<Member> members = memberRepository.findAll();

        for (int i = 1; i <= 4; i++) {
            Ingredient ingredient = new Ingredient();
            ingredient.setName("ingredient " + i);
            ingredientRepository.save(ingredient);
        }
        List<Ingredient> ingredients = ingredientRepository.findAll();

        for (int i = 1; i <= 40; i++) {
            Recipe recipe = new Recipe();
            recipe.setTitle("Recipe " + i);
            recipe.setMember(members.get(i % 5));

            for (int j = 0; j < (i / 10); j++) {
                RecipeIngredient ri = new RecipeIngredient();
                ri.addRecipe(recipe);
                ri.addIngredient(ingredients.get(j));
                ri.setQuantity("??????");
            }

            Image image = new Image();
            image.setImagePath("https://s3.amazon.abcdefg/" + i + ".jpeg");
            recipe.setImage(image);
            int view = (int) ((Math.random() * 10000));
            recipe.setView(view);
            recipeRepository.save(recipe);

            int heartCount = (int) (Math.random() * 100) + 1;
            for (int j = 0; j < heartCount; j++) {
                heartRepository.save(new Heart(members.get(j % 5), recipe));
            }

            int commentCount = (int) (Math.random() * 3);
            for (int j = 0; j < commentCount; j++) {
                Comment comment = Comment.create("comment", members.get(i % 5), recipe);
                commentRepository.save(comment);
            }
        }
    }
    private void initSampleRecipesForRecentRecipes() throws InterruptedException {
        for (int i = 1; i <= 5; i++) {
            Member member = Member.oauth2Builder()
                    .name("member" + i)
                    .profileImagePath("https://s3.aws.abcd/member" + i + ".jpeg")
                    .buildOAuth2Member();
            memberRepository.save(member);
        }
        List<Member> members = memberRepository.findAll();

        for (int i = 1; i <= 4; i++) {
            Ingredient ingredient = new Ingredient();
            ingredient.setName("ingredient " + i);
            ingredientRepository.save(ingredient);
        }
        List<Ingredient> ingredients = ingredientRepository.findAll();

        for (int i = 1; i <= 40; i++) {
            Recipe recipe = new Recipe();
            recipe.setTitle("Recipe " + i);
            recipe.setMember(members.get(i % 5));

            for (int j = 0; j < (i / 10); j++) {
                RecipeIngredient ri = new RecipeIngredient();
                ri.addRecipe(recipe);
                ri.addIngredient(ingredients.get(j));
                ri.setQuantity("??????");
            }

            Image image = new Image();
            image.setImagePath("https://s3.amazon.abcdefg/" + i + ".jpeg");
            recipe.setImage(image);
            int view = (int) ((Math.random() * 10000));
            recipe.setView(view);
//            Thread.sleep(500);
            recipeRepository.save(recipe);

            int heartCount = (int) (Math.random() * 100) + 1;
            for (int j = 0; j < heartCount; j++) {
                heartRepository.save(new Heart(members.get(j % 5), recipe));
            }

            int commentCount = (int) (Math.random() * 3);
            for (int j = 0; j < commentCount; j++) {
                Comment comment = Comment.create("comment", members.get(i % 5), recipe);
                commentRepository.save(comment);
            }
        }
    }
}