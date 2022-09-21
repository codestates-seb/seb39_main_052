package com.seb39.myfridge.comment.service;

import com.seb39.myfridge.comment.entity.Comment;
import com.seb39.myfridge.comment.repository.CommentRepository;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.recipe.service.RecipeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    RecipeService recipeService;
    @Mock
    MemberService memberService;
    @Mock
    CommentRepository commentRepository;
    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("특정 레시피에 댓글을 작성한다")
    void writeComment() throws Exception {
        // given
        Member member = Member.generalBuilder()
                .buildGeneralMember();
        given(memberService.findById(anyLong()))
                .willReturn(member);

        Recipe recipe = new Recipe();
        given(recipeService.findRecipeById(anyLong()))
                .willReturn(recipe);

        // when
        String content = "good recipe. thank you";
        Comment comment = commentService.writeComment(content, 1L, 1L);

        // then
        assertThat(comment.getContent()).isEqualTo(content);
        assertThat(comment.getMember()).isEqualTo(member);
        assertThat(comment.getRecipe()).isEqualTo(recipe);
        assertThat(comment.getRecipe().getComments())
                .anyMatch(c->c.getContent().equals(content));
    }

    @Test
    @DisplayName("자신이 작성한 댓글은 수정 가능하다.")
    void updateComment() throws Exception {
        // given
        Long memberId = 1L;
        Member member = Member.generalBuilder()
                .buildGeneralMember();
        ReflectionTestUtils.setField(member,"id",memberId);
        Recipe recipe = new Recipe();

        Long commentId = 1L;
        Comment comment = Comment.create("good recipe", member, recipe);
        ReflectionTestUtils.setField(comment,"id",commentId);
        given(commentRepository.findById(commentId))
                .willReturn(Optional.of(comment));

        // when
        String newContent = "bad recipe";
        Comment updatedComment = commentService.updateComment(commentId, newContent, memberId);

        // then
        assertThat(updatedComment.getContent()).isEqualTo(newContent);
    }

    @Test
    @DisplayName("자신이 작성하지 않은 댓글은 수정 불가능하다.")
    void updateCommentFail() throws Exception {
        // given
        Long memberId = 1L;
        Member member = Member.generalBuilder()
                .buildGeneralMember();
        ReflectionTestUtils.setField(member,"id",memberId);
        Recipe recipe = new Recipe();

        Long commentId = 1L;
        Comment comment = Comment.create("good recipe", member, recipe);
        ReflectionTestUtils.setField(comment,"id",commentId);
        given(commentRepository.findById(commentId))
                .willReturn(Optional.of(comment));

        // expected
        String newContent = "bad recipe";
        assertThatThrownBy(()-> commentService.updateComment(commentId, newContent, memberId+1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("자신이 작성한 댓글은 삭제 가능하다.")
    void deleteComment() throws Exception {
        // given
        Long memberId = 1L;
        Member member = Member.generalBuilder()
                .buildGeneralMember();
        ReflectionTestUtils.setField(member,"id",memberId);

        Long commentId = 1L;
        Comment comment = Comment.create("good recipe", member, new Recipe());
        ReflectionTestUtils.setField(comment,"id",commentId);
        given(commentRepository.findById(commentId))
                .willReturn(Optional.of(comment));

        // expected
        commentService.deleteComment(commentId, memberId);
    }
    @Test
    @DisplayName("자신이 작성하지 않은 댓글은 삭제 불가능하다.")
    void deleteCommentFail() throws Exception {
        // given
        Long memberId = 1L;
        Member member = Member.generalBuilder()
                .buildGeneralMember();
        ReflectionTestUtils.setField(member,"id",memberId);

        Long commentId = 1L;
        Comment comment = Comment.create("good recipe", member, new Recipe());
        ReflectionTestUtils.setField(comment,"id",commentId);
        given(commentRepository.findById(commentId))
                .willReturn(Optional.of(comment));

        // expected
        assertThatThrownBy(()-> commentService.deleteComment(commentId, memberId+1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}