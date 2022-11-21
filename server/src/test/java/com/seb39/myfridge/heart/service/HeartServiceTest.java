package com.seb39.myfridge.heart.service;

import com.seb39.myfridge.domain.heart.entity.Heart;
import com.seb39.myfridge.domain.heart.repository.HeartRepository;
import com.seb39.myfridge.domain.heart.service.HeartService;
import com.seb39.myfridge.domain.member.entity.Member;
import com.seb39.myfridge.domain.member.service.MemberService;
import com.seb39.myfridge.domain.recipe.entity.Recipe;
import com.seb39.myfridge.domain.recipe.service.RecipeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class HeartServiceTest {

    @Mock
    MemberService memberService;
    @Mock
    RecipeService recipeService;
    @Mock
    HeartRepository heartRepository;

    @InjectMocks
    private HeartService heartService;

    @Test
    @DisplayName("하트가 달리지 않은 레시피에 하트를 추가한다.")
    void addHeartToRecipeTest() throws Exception {
        // given
        Member member = Member.generalBuilder()
                .buildGeneralMember();
        given(memberService.findById(anyLong()))
                .willReturn(member);

        Recipe recipe = new Recipe();
        given(recipeService.findRecipeById(anyLong()))
                .willReturn(recipe);

        given(heartRepository.existsByMemberIdAndRecipeId(anyLong(), anyLong()))
                .willReturn(false);

        // when
        heartService.addHeartToRecipe(1L, 1L);
    }

    @Test
    @DisplayName("한 레시피에 하트는 중복 불가능하다.")
    void addHeartToRecipeFailTest() throws Exception {
        // given
        given(heartRepository.existsByMemberIdAndRecipeId(anyLong(), anyLong()))
                .willReturn(true);

        // expected
        assertThatThrownBy(()->heartService.addHeartToRecipe(1L, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("자신이 하트를 추가한 레시피에서 하트를 제거한다.")
    void removeHeartToRecipeTest() throws Exception {
        // given
        Member member = Member.generalBuilder()
                .buildGeneralMember();
        Recipe recipe = new Recipe();

        Heart heart = new Heart(member, recipe);
        given(heartRepository.findByMemberIdAndRecipeId(anyLong(), anyLong()))
                .willReturn(Optional.of(heart));

        // when
        heartService.removeHeartToRecipe(1L, 1L);

        //then
    }

    @Test
    @DisplayName("존재하지 않는 하트는 제거할 수 없다.")
    void removeHeartToRecipeFailTest() throws Exception {
        // given
        given(heartRepository.findByMemberIdAndRecipeId(anyLong(), anyLong()))
                .willReturn(Optional.empty());

        // expected
        assertThatThrownBy(()-> heartService.removeHeartToRecipe(1L, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("레시피의 전체 하트 개수를 조회한다.")
    void findHeartCountsTest() throws Exception {
        // given
        int heartCount = 123;
        given(heartRepository.countByRecipeId(anyLong()))
                .willReturn(heartCount);

        // when
        int result = heartService.findHeartCounts(1L);

        // then
        assertThat(result).isEqualTo(heartCount);
    }
}