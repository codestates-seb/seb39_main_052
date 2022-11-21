package com.seb39.myfridge.domain.comment.entity;

import com.seb39.myfridge.global.BaseTimeEntity;
import com.seb39.myfridge.domain.member.entity.Member;
import com.seb39.myfridge.domain.recipe.entity.Recipe;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name="comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="recipe_id")
    private Recipe recipe;

    private Comment(String content, Member member, Recipe recipe) {
        this.content = content;
        this.member = member;
        this.recipe = recipe;
    }

    public static Comment create(String content, Member member, Recipe recipe){
        Comment comment = new Comment(content,member,recipe);
        recipe.getComments().add(comment);
        return comment;
    }

    public void updateContent(String content){
        this.content = content;
    }

    public void verifyWriter(Long memberId){
        if(!this.member.getId().equals(memberId))
            throw new IllegalArgumentException("해당 Comment의 작성자가 아닙니다. commentId = " + this.getId() + " memberId = " + memberId);
    }
}
