package com.seb39.myfridge.comment.service;


import com.seb39.myfridge.comment.entity.Comment;
import com.seb39.myfridge.comment.repository.CommentRepository;
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
public class CommentService {

    private final MemberService memberService;
    private final RecipeService recipeService;
    private final CommentRepository commentRepository;

    @Transactional
    public Comment writeComment(String content, Long memberId, Long recipeId) {
        Recipe recipe = recipeService.findRecipeById(recipeId);
        Member member = memberService.findById(memberId);
        Comment comment = Comment.create(content, member, recipe);
        commentRepository.save(comment);
        return comment;
    }

    @Transactional
    public Comment updateComment(Long commentId, String content, Long memberId) {
        verifyWriter(commentId, memberId);
        Comment comment = findComment(commentId);
        comment.updateContent(content);
        return comment;
    }

    private Comment findComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not exist. id = " + id));
    }

    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        verifyWriter(commentId, memberId);
        commentRepository.deleteById(commentId);
    }

    private void verifyWriter(Long commentId, Long memberId) {
        Comment comment = findComment(commentId);
        Long writerId = comment.getMember().getId();
        if (!writerId.equals(memberId))
            throw new IllegalArgumentException("작성자가 아니면 수정할 수 없습니다.");
    }
}
