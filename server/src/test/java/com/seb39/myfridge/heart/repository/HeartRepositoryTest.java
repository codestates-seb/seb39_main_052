package com.seb39.myfridge.heart.repository;

import com.seb39.myfridge.domain.heart.repository.HeartRepository;
import com.seb39.myfridge.global.config.QueryDslConfig;
import com.seb39.myfridge.domain.heart.entity.Heart;
import com.seb39.myfridge.domain.member.entity.Member;
import com.seb39.myfridge.domain.member.repository.MemberRepository;
import com.seb39.myfridge.domain.recipe.entity.Recipe;
import com.seb39.myfridge.domain.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;


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
        for (int i = 0; i < heartCount; i++) {
            heartRepository.save(new Heart(null, recipe));
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

    @Test
    @DisplayName("findByRecipeIds test")
    void findByMemberAndrecipeIdsTest() throws Exception {
        // given
        for(int i=1;i<=2;i++){
            Member member = Member.generalBuilder()
                    .name("member" + i)
                    .buildGeneralMember();
            memberRepository.save(member);
        }

        List<Member> members = memberRepository.findAll();
        for (int i = 1; i <= 10; i++) {
            Recipe recipe = new Recipe();
            Member member = members.get(i%2);
            recipe.setTitle("Test recipe" + i);
            recipe.setMember(member);
            recipeRepository.save(recipe);
            Heart heart = new Heart(member, recipe);
            heartRepository.save(heart);
        }
        List<Long> recipeIds = recipeRepository.findAll().stream()
                .map(r -> r.getId())
                .collect(Collectors.toList());

        em.flush();
        em.clear();

        // when
        List<Heart> hearts = heartRepository.findByMemberIdAndRecipeIds(members.get(0).getId(), recipeIds);

        // then
        for (Heart heart : hearts) {
            System.out.println("member, recipe : " + heart.getMember().getId()+ "  " + heart.getRecipe().getId());
        }
    }
}