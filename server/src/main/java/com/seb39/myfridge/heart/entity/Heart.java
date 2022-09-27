package com.seb39.myfridge.heart.entity;

import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.recipe.entity.Recipe;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Heart {
    @Id @GeneratedValue
    @Column(name="heart_id")
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
