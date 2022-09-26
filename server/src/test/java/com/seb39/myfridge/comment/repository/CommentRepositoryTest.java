package com.seb39.myfridge.comment.repository;

import com.seb39.myfridge.comment.entity.Comment;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.repository.MemberRepository;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

@DataJpaTest
@Transactional
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    EntityManager em;

    // @Test
    void findAllByRecipeWriterQueryTest() {
        // given
        Member member1 = Member.generalBuilder()
                .name("member1")
                .buildGeneralMember();
        memberRepository.save(member1);

        Member member2 = Member.generalBuilder()
                .name("member1")
                .buildGeneralMember();
        memberRepository.save(member2);

        for (int i = 1; i <= 100; i++) {
            if (i % 2 == 0) {
                Recipe recipe1 = new Recipe();
                recipe1.setTitle("Test recipe" + i);
                recipe1.setMember(member1);
                recipeRepository.save(recipe1);
                commentRepository.save(Comment.create("comment" + i, member1, recipe1));
            } else {
                Recipe recipe2 = new Recipe();
                recipe2.setTitle("Test recipe" + i);
                recipe2.setMember(member2);
                recipeRepository.save(recipe2);
                commentRepository.save(Comment.create("comment" + i, member2, recipe2));
            }
        }

        em.flush();
        em.clear();

        Page<Comment> page = commentRepository.findAllByRecipeWriterId(member1.getId(), PageRequest.of(0, 16, Sort.by("id").descending()));
        List<Comment> content = page.getContent();

        System.out.println("size = " + content.size());
        for (Comment comment : content) {
            System.out.println(comment.getRecipe().getTitle() + " " + comment.getContent());
        }
    }

    @Test
    void findAllByRecipeIdTest() {
        // given
        Member member1 = Member.generalBuilder()
                .name("member1")
                .buildGeneralMember();
        memberRepository.save(member1);

        Recipe recipe1 = new Recipe();
        recipe1.setTitle("Test recipe");
        recipe1.setMember(member1);
        recipeRepository.save(recipe1);

        for (int i = 1; i <= 25; i++) {
            commentRepository.save(Comment.create("comment" + i, member1, recipe1));
        }

        em.flush();
        em.clear();

        Page<Comment> page = commentRepository.findAllByRecipeId(recipe1.getId(), PageRequest.of(0, 10, Sort.by("id").descending()));
        List<Comment> content = page.getContent();

        System.out.println("size = " + content.size());
        for (Comment comment : content) {
            System.out.println(comment.getRecipe().getTitle() + " " + comment.getContent());
        }
    }
}