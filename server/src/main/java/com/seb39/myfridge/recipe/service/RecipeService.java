package com.seb39.myfridge.recipe.service;

import com.seb39.myfridge.image.entity.Image;
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
import com.seb39.myfridge.image.upload.FileUploadService;
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


    @Transactional
    public Recipe createRecipe(Recipe recipe, List<Step> steps, Long memberId, List<MultipartFile> files, List<RecipeIngredient> recipeIngredients) {
        Member member = memberService.findById(memberId);
        recipe.setMember(member);

        ingredientService.createIngredient(recipe, recipeIngredients);

//        createImage(recipe, files, steps);
        System.out.println("recipe.getRecipeIngredients().size() = " + recipe.getRecipeIngredients().size());
        Recipe savedRecipe = recipeRepository.save(recipe);
        steps.forEach(step -> step.addRecipe(recipe));
        return savedRecipe;
    }

    @Transactional
    public Recipe updateRecipe(Recipe recipe, List<Step> steps, Long memberId, List<MultipartFile> files, List<RecipeIngredient> recipeIngredients ) {
        Recipe findRecipe = findRecipeById(recipe.getId());
        verifyWriter(findRecipe, memberId);

        Optional.ofNullable(recipe.getTitle()).ifPresent(findRecipe::setTitle);
        Optional.ofNullable(recipe.getTime()).ifPresent(findRecipe::setTime);
        Optional.ofNullable(recipe.getPortion()).ifPresent(findRecipe::setPortion);

        findRecipe.getImage().setIsUpdated(recipe.getImage().getIsUpdated());

        findRecipe.getRecipeIngredients().clear();
        ingredientService.updateIngredient(findRecipe, recipeIngredients);

        //1. 이미지 삭제
//        deleteImage(findRecipe);
        findRecipe.getSteps().clear();
        System.out.println("updateImage 이전");
        updateImage(findRecipe, files, steps);
        //2. 이미지 재업로드
//        createImage(findRecipe, files, steps);

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


    /**
     * Image
     */
    @Transactional
    public Recipe createRecipeImage(Recipe recipe, List<Step> steps, Long memberId, List<MultipartFile> files, List<RecipeIngredient> recipeIngredients) {
        Member member = memberService.findById(memberId);
        recipe.setMember(member);

        ingredientService.createIngredient(recipe, recipeIngredients);


        //recipe 이미지 지정
        fileUploadService.uploadImages(recipe, steps, files);

//        createImage(recipe, files, steps);
        Recipe savedRecipe = recipeRepository.save(recipe);
        steps.forEach(step -> step.addRecipe(recipe));
        return savedRecipe;
    }

    private void updateImage(Recipe findRecipe, List<MultipartFile> files, List<Step> steps) {
        if (!CollectionUtils.isEmpty(files)) {
            if (findRecipe.getImage().getIsUpdated().equals("Y")) {
                int idx = findRecipe.getImage().getIdx();
                System.out.println("idx = " + idx);
                fileUploadService.updateImages(findRecipe, steps, files, idx);
            }else {
                for (Step step : steps) {
                    if (step.getImage().getIsUpdated().equals("Y")) {
                        int idx = step.getImage().getIdx();
                        System.out.println("idx = " + idx);
                        fileUploadService.updateImages(findRecipe, steps, files, idx);
                    }
                }
            }
        }
    }


    //이미지 생성 메서드
   /* private void createImage(Recipe findRecipe, List<MultipartFile> files, List<Step> steps) {
        if (!CollectionUtils.isEmpty(files)) {
            if (findRecipe.getImagePath() == null) {
                findRecipe.setImagePath(fileUploadService.uploadImage(files.get(0)));
                files.remove(0);
            }
            for (Step step : steps) {
                if (step.getImagePath() == null) {
                    step.setImagePath(fileUploadService.uploadImage(files.get(0)));
                    files.remove(0);
                }
            }
            //첫 번째 레시피 사진은 비어있고, 두 번째 레시피 사진을 넣고싶다면???
        }
    }

    //이미지 삭제 메서드
    private void deleteImage(Recipe findRecipe) {
        fileUploadService.deleteImage(findRecipe.getImagePath());
        List<Step> steps = findRecipe.getSteps();
        for (Step step : steps) {
            if (step.getImagePath() != null) {
                fileUploadService.deleteImage(step.getImagePath());
            }
        }
    }*/

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
}
