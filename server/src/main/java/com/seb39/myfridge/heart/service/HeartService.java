package com.seb39.myfridge.heart.service;

import com.seb39.myfridge.heart.entity.Heart;
import com.seb39.myfridge.heart.repository.HeartRepository;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HeartService {

    private final MemberService memberService;
    private final RecipeService recipeService;
    private final HeartRepository heartRepository;

    @Transactional
    public void addHeartToRecipe(Long memberId, Long recipeId){
        verifyAddHeartPossible(memberId,recipeId);
        addHeart(memberId,recipeId);
    }

    @Transactional
    public void removeHeartToRecipe(Long memberId, Long recipeId){
        Heart heart = findHeart(memberId, recipeId);
        removeHeart(heart);
    }

    public int findHeartCounts(Long recipeId){
        return heartRepository.countByRecipeId(recipeId);
    }

    private Heart findHeart(Long memberId, Long recipeId){
        return heartRepository.findByMemberIdAndRecipeId(memberId, recipeId)
                .orElseThrow(()-> new IllegalArgumentException("heart not exist. member = " + memberId + " recipe = " + recipeId));
    }

    private void verifyAddHeartPossible(Long memberId, Long recipeId) {
        if(heartRepository.existsByMemberIdAndRecipeId(memberId,recipeId))
            throw new IllegalArgumentException("heart is already marked");
    }

    private void addHeart(Long memberId, Long recipeId){
        Member member = memberService.findById(memberId);
        Recipe recipe = recipeService.findRecipeById(recipeId);
        heartRepository.save(new Heart(member, recipe));
    }

    private void removeHeart(Heart heart){
        heartRepository.delete(heart);
    }
}
