package com.seb39.myfridge.domain.heart.entity;

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
public class Heart extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "heart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    public Heart(Member member, Recipe recipe) {
        this.member = member;
        this.recipe = recipe;
    }
}
