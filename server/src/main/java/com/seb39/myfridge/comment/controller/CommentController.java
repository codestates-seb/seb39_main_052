package com.seb39.myfridge.comment.controller;


import com.seb39.myfridge.auth.PrincipalDetails;
import com.seb39.myfridge.comment.dto.CommentDto;
import com.seb39.myfridge.comment.entity.Comment;
import com.seb39.myfridge.comment.mapper.CommentMapper;
import com.seb39.myfridge.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.seb39.myfridge.comment.mapper.CommentMapper.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/recipes/{recipeId}/comments")
    @Secured("ROLE_USER")
    public ResponseEntity<CommentDto.Response> postComment(@RequestBody CommentDto.Post dto, @PathVariable Long recipeId, @AuthenticationPrincipal PrincipalDetails principal){
        Long memberId = principal.getMemberId();
        Comment comment = commentService.writeComment(dto.getContent(), memberId, recipeId);
        CommentDto.Response response = commentToResponseDto(comment);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/api/comments/{commentId}")
    @Secured("ROLE_USER")
    public ResponseEntity<CommentDto.Response> patchComment(@RequestBody CommentDto.Patch dto, @PathVariable Long commentId, @AuthenticationPrincipal PrincipalDetails principal){
        Long memberId = principal.getMemberId();
        Comment comment = commentService.updateComment(commentId, dto.getContent(), memberId);
        CommentDto.Response response = commentToResponseDto(comment);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/api/comments/{commentId}")
    @Secured("ROLE_USER")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal PrincipalDetails principal){
        Long memberId = principal.getMemberId();
        commentService.deleteComment(commentId,memberId);
        return ResponseEntity.noContent().build();
    }
}
