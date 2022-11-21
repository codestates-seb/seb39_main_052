package com.seb39.myfridge.domain.recipe.service;

import com.seb39.myfridge.domain.fridge.entity.FridgeIngredient;
import com.seb39.myfridge.domain.fridge.service.FridgeIngredientService;
import com.seb39.myfridge.domain.heart.repository.HeartRepository;
import com.seb39.myfridge.domain.recipe.dto.RecipeSearch;
import com.seb39.myfridge.domain.recipe.entity.Recipe;
import com.seb39.myfridge.domain.recipe.enums.RecipeSort;
import com.seb39.myfridge.domain.image.upload.FileUploadService;
import com.seb39.myfridge.domain.ingredient.Repository.IngredientRepository;
import com.seb39.myfridge.domain.ingredient.Repository.RecipeIngredientRepository;
import com.seb39.myfridge.domain.ingredient.entity.RecipeIngredient;
import com.seb39.myfridge.domain.ingredient.service.IngredientService;
import com.seb39.myfridge.domain.member.entity.Member;
import com.seb39.myfridge.domain.member.service.MemberService;
import com.seb39.myfridge.domain.recipe.dto.MyRecipeDto;
import com.seb39.myfridge.domain.recipe.dto.RecipeRecommendDto;
import com.seb39.myfridge.domain.recipe.repository.RecipeRepository;
import com.seb39.myfridge.domain.recipe.entity.Step;
import com.seb39.myfridge.domain.recipe.repository.StepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final StepRepository stepRepository;
    private final MemberService memberService;
    private final FileUploadService fileUploadService;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientService ingredientService;
    private final HeartRepository heartRepository;
    private final FridgeIngredientService fridgeIngredientService;


    public Recipe findRecipeWithDetails(Long recipeId) {
        Recipe recipe = recipeRepository.findWithDetails(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("Recipe not exist. id = " + recipeId));
        // recipe.ingredients 영속화를 위한 호출
        recipeRepository.findWithIngredients(recipeId);
        return recipe;
    }

    @Transactional
    public Recipe createRecipe(Recipe recipe, List<Step> steps, Long memberId, List<MultipartFile> files, List<RecipeIngredient> recipeIngredients) {
        Member member = memberService.findById(memberId);
        recipe.setMember(member);

        ingredientService.createIngredient(recipe, recipeIngredients);

        fileUploadService.uploadImages(recipe, steps, files);

        Recipe savedRecipe = recipeRepository.save(recipe);
        steps.forEach(step -> step.addRecipe(recipe));
        return savedRecipe;
    }


    @Transactional
    public Recipe updateRecipe(Recipe recipe, List<Step> steps, Long memberId, List<MultipartFile> files, List<RecipeIngredient> recipeIngredients) {
        Recipe findRecipe = findRecipeById(recipe.getId());
        findRecipe.verifyWriter(memberId);

        Optional.ofNullable(recipe.getTitle()).ifPresent(findRecipe::setTitle);
        Optional.ofNullable(recipe.getTime()).ifPresent(findRecipe::setTime);
        Optional.of(recipe.getPortion()).ifPresent(findRecipe::setPortion);

        findRecipe.getImage().setIsUpdated(recipe.getImage().getIsUpdated());

        findRecipe.getRecipeIngredients().clear();
        ingredientService.updateIngredient(findRecipe, recipeIngredients);

        findRecipe.getSteps().clear();
        updateImage(findRecipe, files, steps);

        stepRepository.deleteStepByRecipeId(findRecipe.getId());
        steps.forEach(step -> step.addRecipe(findRecipe));
        return recipeRepository.save(findRecipe);
    }

    @Transactional
    public void deleteRecipe(Long id, Long memberId) {
        Recipe recipe = findRecipeById(id);
        verifyCanDelete(recipe, memberId);
        recipeRepository.delete(recipe);
    }

    @Transactional
    public void addView(Long recipeId){
        recipeRepository.addView(recipeId);
    }

    private void verifyCanDelete(Recipe recipe, Long memberId) {
        Member member = memberService.findById(memberId);
        boolean isAdmin = member.getRoleList()
                .stream()
                .anyMatch(role -> role.equals("ROLE_ADMIN"));

        if(!isAdmin)
            recipe.verifyWriter(memberId);
    }


    private void updateImage(Recipe findRecipe, List<MultipartFile> files, List<Step> steps) {
        if (!CollectionUtils.isEmpty(files)) {
            for (MultipartFile file : files) {
                if (findRecipe.getImage().getIsUpdated().equals("Y")) {
                    fileUploadService.updateRecipeImages(findRecipe, file);
                } else {
                    for (Step step : steps) {
                        if (step.getImage().getIsUpdated().equals("Y")) {
                            int idx = step.getImage().getIdx();
                            fileUploadService.updateStepImages(step, file, idx);
                            break;
                        }
                    }
                }
            }
        }
    }

    public Recipe findRecipeById(Long recipeId) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        return optionalRecipe.orElseThrow(() -> new IllegalArgumentException("해당 레시피를 찾을 수 없습니다."));
    }

    public List<String> findTitlesByContainsWord(String word) {
        List<String> result = recipeRepository.searchTitles(word);
        return result;
    }

    public Page<RecipeSearch.Response> searchRecipes(RecipeSearch.Request request) {
        return recipeRepository.searchRecipes(request);
    }

    public Page<MyRecipeDto.Mine> findMyRecipes(Long memberId, int page, RecipeSort sort) {
        return recipeRepository.findMyRecipes(memberId, page, sort);
    }

    public Page<MyRecipeDto.Favorite> findFavoriteRecipes(Long memberId, int page) {
        return recipeRepository.findFavoriteRecipes(memberId, page);
    }

    public List<RecipeRecommendDto> findPopularRecipes() {
        return recipeRepository.findPopularRecipes();
    }

    public List<RecipeRecommendDto> findRecentRecipes() {
        return recipeRepository.findRecentRecipes();
    }

    public List<RecipeRecommendDto> recommendByFridge(Long fridgeId) {
        List<FridgeIngredient> fridgeIngredients = fridgeIngredientService.findFridgeIngredient(fridgeId);
        if(fridgeIngredients.isEmpty())
            return List.of();

        List<String> ingredientNames = fridgeIngredients.stream()
                .map(fi -> fi.getIngredient().getName())
                .collect(Collectors.toList());
        return recipeRepository.recommendByIngredientNames(ingredientNames);
    }
}