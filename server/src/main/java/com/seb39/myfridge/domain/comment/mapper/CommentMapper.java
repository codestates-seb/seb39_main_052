package com.seb39.myfridge.domain.comment.mapper;

import com.seb39.myfridge.domain.comment.dto.CommentDto;
import com.seb39.myfridge.domain.comment.entity.Comment;
import com.seb39.myfridge.domain.member.dto.MemberDto;

public interface CommentMapper {
    static CommentDto.Response commentToResponseDto(Comment comment){
        return CommentDto.Response.builder()
                .member(new MemberDto.Response(comment.getMember()))
                .recipeId(comment.getRecipe().getId())
                .commentId(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
