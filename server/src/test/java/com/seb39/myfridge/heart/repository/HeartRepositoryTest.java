package com.seb39.myfridge.heart.repository;

import com.seb39.myfridge.config.QueryDslConfig;
import com.seb39.myfridge.heart.entity.Heart;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.repository.MemberRepository;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.repository.RecipeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Transactional
@Import(QueryDslConfig.class)
class HeartRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    HeartRepository heartRepository;

    @Autowired
    EntityManager em;


    @Test
    @DisplayName("heart가 있을 때 existsByMemberIdAndRecipeId가 true를 반환한다.")
    void existHeart() throws Exception {
        // given
        Member member = Member.generalBuilder()
                .name("SJ")
                .buildGeneralMember();
        memberRepository.save(member);

        Recipe recipe = new Recipe();
        recipe.setTitle("Test recipe");
        recipe.setMember(member);
        recipeRepository.save(recipe);

        Heart heart = new Heart(member, recipe);
        heartRepository.save(heart);

        // when
        boolean result = heartRepository.existsByMemberIdAndRecipeId(member.getId(), recipe.getId());

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("heart가 없을 때 existsByMemberIdAndRecipeId가 false  반환한다.")
    void notExistHeart() throws Exception {
        // given
        Member member = Member.generalBuilder()
                .name("SJ")
                .buildGeneralMember();
        memberRepository.save(member);

        Recipe recipe = new Recipe();
        recipe.setTitle("Test recipe");
        recipe.setMember(member);
        recipeRepository.save(recipe);

        // when
        boolean result = heartRepository.existsByMemberIdAndRecipeId(member.getId(), recipe.getId());

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("특정 레시피의 heart 개수를 반환한다.")
    void countHeartsByRecipe() throws Exception {
        // given
        Member member = Member.generalBuilder()
                .name("SJ")
                .buildGeneralMember();
        memberRepository.save(member);

        Recipe recipe = new Recipe();
        recipe.setTitle("Test recipe");
        recipe.setMember(member);
        recipeRepository.save(recipe);

        int heartCount = 17;
        for(int i=0;i<heartCount;i++){
            heartRepository.save(new Heart(null,recipe));
        }

        // when
        int result = heartRepository.countByRecipeId(recipe.getId());

        // then
        assertThat(result).isEqualTo(heartCount);
    }

    @Test
    @DisplayName("findByMemberIdAndRecipeId test")
    void findHeart() throws Exception {
        // given
        Member member = Member.generalBuilder()
                .name("SJ")
                .buildGeneralMember();
        memberRepository.save(member);

        Recipe recipe = new Recipe();
        recipe.setTitle("Test recipe");
        recipe.setMember(member);
        recipeRepository.save(recipe);

        Heart heart = new Heart(member, recipe);
        heartRepository.save(heart);

        // when
        Optional<Heart> optionalHeart = heartRepository.findByMemberIdAndRecipeId(member.getId(), recipe.getId());

        // then
        assertThat(optionalHeart.isPresent()).isTrue();
    }
}