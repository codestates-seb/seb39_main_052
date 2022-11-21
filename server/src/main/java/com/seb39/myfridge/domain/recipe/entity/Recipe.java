package com.seb39.myfridge.domain.recipe.entity;

import com.seb39.myfridge.domain.comment.entity.Comment;
import com.seb39.myfridge.domain.heart.entity.Heart;
import com.seb39.myfridge.global.BaseTimeEntity;
import com.seb39.myfridge.domain.image.entity.Image;
import com.seb39.myfridge.domain.ingredient.entity.RecipeIngredient;
import com.seb39.myfridge.domain.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Recipe extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Long id;

    private String title;

    @ColumnDefault("0")
    private int view;

    private int portion;

    private String time;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Step> steps = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.REMOVE)
    private List<Heart> hearts = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    public void verifyWriter(Long memberId){
        if(!member.getId().equals(memberId))
            throw new IllegalArgumentException("작성자가 아닙니다. 검증요청 id = " + memberId + ", 작성자 id = " + member.getId());
    }
}
