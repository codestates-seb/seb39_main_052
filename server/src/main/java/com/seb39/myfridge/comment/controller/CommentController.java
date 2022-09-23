package com.seb39.myfridge.comment.controller;


import com.seb39.myfridge.auth.PrincipalDetails;
import com.seb39.myfridge.auth.annotation.AuthMemberId;
import com.seb39.myfridge.comment.dto.CommentDto;
import com.seb39.myfridge.comment.entity.Comment;
import com.seb39.myfridge.comment.mapper.CommentMapper;
import com.seb39.myfridge.comment.service.CommentService;
import com.seb39.myfridge.dto.MultiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.seb39.myfridge.comment.mapper.CommentMapper.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/comments/received")
    @Secured("ROLE_USER")
    public ResponseEntity<MultiResponseDto<CommentDto.Response>> getReceivedComments(@RequestParam("page") int page, @AuthMemberId Long memberId){
        Page<Comment> result = commentService.findReceivedComments(page - 1, memberId);
        List<CommentDto.Response> data = result.getContent().stream()
                .map(CommentMapper::commentToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new MultiResponseDto<>(data,result));
    }

    @PostMapping("/api/recipes/{recipeId}/comments")
    @Secured("ROLE_USER")
    public ResponseEntity<CommentDto.Response> postComment(@RequestBody CommentDto.Post dto, @PathVariable Long recipeId, @AuthMemberId Long memberId){
        Comment comment = commentService.writeComment(dto.getContent(), memberId, recipeId);
        CommentDto.Response response = commentToResponseDto(comment);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/api/comments/{commentId}")
    @Secured("ROLE_USER")
    public ResponseEntity<CommentDto.Response> patchComment(@RequestBody CommentDto.Patch dto, @PathVariable Long commentId, @AuthMemberId Long memberId){
        Comment comment = commentService.updateComment(commentId, dto.getContent(), memberId);
        CommentDto.Response response = commentToResponseDto(comment);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/api/comments/{commentId}")
    @Secured("ROLE_USER")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, @AuthMemberId Long memberId){
        commentService.deleteComment(commentId,memberId);
        return ResponseEntity.noContent().build();
    }
}
