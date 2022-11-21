package com.seb39.myfridge.domain.comment.service;


import com.seb39.myfridge.domain.comment.entity.Comment;
import com.seb39.myfridge.domain.comment.repository.CommentRepository;
import com.seb39.myfridge.domain.member.entity.Member;
import com.seb39.myfridge.domain.member.service.MemberService;
import com.seb39.myfridge.domain.recipe.entity.Recipe;
import com.seb39.myfridge.domain.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final MemberService memberService;
    private final RecipeService recipeService;
    private final CommentRepository commentRepository;

    @Value("${app.pageable.size.comment}")
    private int size;

    public Page<Comment> findByRecipeId(Long recipeId, int page){
        Pageable pageable = PageRequest.of(page,size, Sort.by("id").descending());
        return commentRepository.findAllByRecipeId(recipeId, pageable);
    }

    public Page<Comment> findReceivedComments(Long memberId, int page){
        Pageable pageable = PageRequest.of(page,size, Sort.by("id").descending());
        return commentRepository.findAllByRecipeWriterId(memberId, pageable);
    }

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
        Comment comment = findComment(commentId);
        comment.verifyWriter(memberId);
        comment.updateContent(content);
        return comment;
    }

    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = findComment(commentId);
        comment.verifyWriter(memberId);
        commentRepository.deleteById(commentId);
    }

    private Comment findComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not exist. id = " + id));
    }
}
