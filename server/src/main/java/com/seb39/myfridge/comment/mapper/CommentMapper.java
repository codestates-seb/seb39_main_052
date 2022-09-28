package com.seb39.myfridge.comment.mapper;

import com.seb39.myfridge.comment.dto.CommentDto;
import com.seb39.myfridge.comment.entity.Comment;

public interface CommentMapper {
    static CommentDto.Response commentToResponseDto(Comment comment){
        return CommentDto.Response.builder()
                .memberId(comment.getMember().getId())
                .memberName(comment.getMember().getName())
                .memberImagePath(comment.getMember().getProfileImagePath())
                .recipeId(comment.getRecipe().getId())
                .commentId(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}