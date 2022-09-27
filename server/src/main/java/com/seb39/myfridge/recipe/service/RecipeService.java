package com.seb39.myfridge.recipe.service;

import com.seb39.myfridge.image.upload.FileUploadService;
import com.seb39.myfridge.ingredient.Repository.IngredientRepository;
import com.seb39.myfridge.ingredient.Repository.RecipeIngredientRepository;
import com.seb39.myfridge.ingredient.entity.RecipeIngredient;
import com.seb39.myfridge.ingredient.service.IngredientService;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.repository.RecipeRepository;
import com.seb39.myfridge.step.entity.Step;
import com.seb39.myfridge.step.repository.StepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public Recipe findRecipeWithDetails(Long recipeId){
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

        //recipe 이미지 지정
        fileUploadService.uploadImages(recipe, steps, files);

        Recipe savedRecipe = recipeRepository.save(recipe);
        steps.forEach(step -> step.addRecipe(recipe));
        return savedRecipe;
    }


    @Transactional
    public Recipe updateRecipe(Recipe recipe, List<Step> steps, Long memberId, List<MultipartFile> files, List<RecipeIngredient> recipeIngredients) {
        Recipe findRecipe = findRecipeById(recipe.getId());
        verifyWriter(findRecipe, memberId);

        Optional.ofNullable(recipe.getTitle()).ifPresent(findRecipe::setTitle);
        Optional.ofNullable(recipe.getTime()).ifPresent(findRecipe::setTime);
        Optional.ofNullable(recipe.getPortion()).ifPresent(findRecipe::setPortion);

        findRecipe.getImage().setIsUpdated(recipe.getImage().getIsUpdated());

        findRecipe.getRecipeIngredients().clear();
        ingredientService.updateIngredient(findRecipe, recipeIngredients);

        findRecipe.getSteps().clear();
        updateImage(findRecipe, files, steps);

        //update를 하면 기존의 step이 중복으로 들어가는 문제 발생 -> update를 하기 이전에, step을 삭제(더 좋은 방법이 있을까?)
        stepRepository.deleteStepByRecipeId(findRecipe.getId());
        steps.forEach(step -> step.addRecipe(findRecipe));
        Recipe updateRecipe = recipeRepository.save(findRecipe);
        return updateRecipe;
    }


    @Transactional
    public void deleteRecipe(long id, long memberId) {
        Recipe findRecipe = findRecipeById(id);
        verifyWriter(findRecipe, memberId);
        //s3 버킷에서 해당 레시피에 관련된 이미지 삭제
//        deleteImage(findRecipe);
        recipeRepository.delete(findRecipe);
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

    public void verifyWriter(Recipe recipe, Long memberId) {
        Long writerId = recipe.getMember().getId();
        if (!Objects.equals(writerId, memberId)) {
            throw new IllegalArgumentException("작성자가 아니면 수정/삭제할 수 없습니다!");
        }
    }

    public List<String> findTitlesByContainsWord(String word){
        List<String> result = recipeRepository.searchTitles(word);
        return result;
    }
}
