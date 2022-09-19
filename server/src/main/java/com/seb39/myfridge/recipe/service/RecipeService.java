package com.seb39.myfridge.recipe.service;

import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.repository.RecipeRepository;
import com.seb39.myfridge.step.entity.Step;
import com.seb39.myfridge.step.repository.StepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final StepRepository stepRepository;
    private final MemberService memberService;

    @Transactional
    public Recipe createRecipe(Recipe recipe, List<Step> steps, Long memberId) {
        //변경해야 할 사항
        //1. 레시피를 등록한 member 값 저장
        //2. 레시피를 등록할 때 입력한 재료 저장
        Member member = memberService.findById(memberId);
        recipe.setMember(member);
        Recipe savedRecipe = recipeRepository.save(recipe);
        steps.forEach(step -> step.addRecipe(recipe));
        return savedRecipe;
    }

    @Transactional
    public Recipe updateRecipe(Recipe recipe, List<Step> steps, Long memberId) {
        Recipe findRecipe = findRecipeById(recipe.getId());

        verifyWriter(findRecipe, memberId);

        Optional.ofNullable(recipe.getTitle()).ifPresent(findRecipe::setTitle);

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
        //삭제하는 사람이 recipe 작성자인지 검색하는 로직 필요
        recipeRepository.delete(findRecipe);
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
}